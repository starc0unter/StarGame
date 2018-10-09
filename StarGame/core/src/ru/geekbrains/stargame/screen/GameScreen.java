package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.stargame.base.ActionListener;
import ru.geekbrains.stargame.base.Base2DScreen;
import ru.geekbrains.stargame.base.Font;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.EnemyPool;
import ru.geekbrains.stargame.pool.ExplosionPool;
import ru.geekbrains.stargame.sprites.Background;
import ru.geekbrains.stargame.sprites.Bullet;
import ru.geekbrains.stargame.sprites.ButtonStartNewGame;
import ru.geekbrains.stargame.sprites.Enemy;
import ru.geekbrains.stargame.sprites.MainShip;
import ru.geekbrains.stargame.sprites.MessageGameOver;
import ru.geekbrains.stargame.sprites.Star;
import ru.geekbrains.stargame.utils.EnemiesEmitter;

import static com.badlogic.gdx.graphics.Color.DARK_GRAY;


public class GameScreen extends Base2DScreen implements ActionListener {

    private static final int STAR_COUNT = 80;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "LEVEL: ";

    private enum State {PLAYING, GAME_OVER}

    Background background;
    Texture bg;
    TextureAtlas atlas;
    TextureAtlas astroAtlas;

    Star[] star;

    MainShip mainShip;

    Music music;
    Sound gameOverSound;
    Sound laserSound;
    Sound bulletSound;
    Sound explosionSound;
    Sound selected;

    Font font;

    StringBuilder sbFrags = new StringBuilder();
    StringBuilder sbHP = new StringBuilder();
    StringBuilder sbLevel = new StringBuilder();

    int frags;

    EnemiesEmitter enemiesEmitter;

    BulletPool bulletPool;
    EnemyPool enemyPool;
    ExplosionPool explosionPool;

    State state;

    MessageGameOver messageGameOver;
    ButtonStartNewGame buttonStartNewGame;

    private float blinkInterval = 1f;
    private float blinkTimer;
    private float colorStep = 0.01f;
    private float currentColor = 1f;
    private float targetColor = 0.1f;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/mainTheme.mp3"));
        music.setVolume(0.4f);
        music.setLooping(true);

        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gameOver.wav"));
        selected = Gdx.audio.newSound(Gdx.files.internal("sounds/selected.wav"));

        bg = new Texture("bg.png");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        astroAtlas = new TextureAtlas("textures/astro.tpack");

        font = new Font("font/font.fnt", "font/font.png");
        font.setFontSize(0.035f);

        star = new Star[STAR_COUNT];
        for (int i = 0; i < star.length; i++) {
            if (i % 4 == 0) star[i] = new Star(astroAtlas, "star", 0.04f, 0.06f);
            else if (i % 4 == 1)  star[i] = new Star(atlas, "star", 0.01f, 0.02f);
            else star[i] = new Star(astroAtlas, "blueStar", 0.06f, 0.1f);
        }

        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        mainShip = new MainShip(atlas, bulletPool, explosionPool, laserSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, bulletSound, mainShip);

        enemiesEmitter = new EnemiesEmitter(enemyPool, atlas, worldBounds);
        messageGameOver = new MessageGameOver(atlas);
        buttonStartNewGame = new ButtonStartNewGame(atlas, this);
        startNewGame();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        deleteAllDestroyed();
        draw();
    }

    public void update(float delta) {
        for (int i = 0; i < star.length; i++) {
            star[i].update(delta);
        }
        explosionPool.updateActiveObjects(delta);
        bulletPool.updateActiveObjects(delta);
        if (mainShip.isDestroyed()) {
            if (state != State.GAME_OVER) {
                music.stop();
                gameOverSound.play(15f);
            }
            state = State.GAME_OVER;
            targetColor = 0.1f;
        }
        switch (state) {
            case PLAYING:
                mainShip.update(delta);
                enemyPool.updateActiveObjects(delta);
                enemiesEmitter.generateEnemies(delta, frags);
                if (enemiesEmitter.isLeveledUp()) {
                    mainShip.setHp(MainShip.INITIAL_HP);
                    enemiesEmitter.setLeveledUp(false);
                }
                break;
            case GAME_OVER:
                blinkTimer += delta;
                break;
        }
    }

    public void checkCollisions() {
        // Столкновение кораблей
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if (enemy.pos.dst2(mainShip.pos) < minDist * minDist) {
                enemy.destroy();
                enemy.boom();
                mainShip.damage(10 * enemy.getBulletDamage());
                return;
            }
        }

        // Попадание пули во вражеский корабль
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != mainShip || bullet.isDestroyed()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    bullet.destroy();
                    enemy.damage(bullet.getDamage());
                    if (enemy.isDestroyed()) frags++;
                }
            }
        }

        // Попадание пули в игровой корабль
        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() == mainShip || bullet.isDestroyed()) {
                continue;
            }
            if (mainShip.isBulletCollision(bullet)) {
                mainShip.damage(bullet.getDamage());
                bullet.destroy();
            }
        }
    }

    public void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    public void draw() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < star.length; i++) {
            star[i].draw(batch);
        }
        bulletPool.drawActiveObjects(batch);
        mainShip.draw(batch);
        enemyPool.drawActiveObjects(batch);
        explosionPool.drawActiveObjects(batch);
        printInfo();
        batch.setColor(1, 1, 1, 1);
        if (state == State.GAME_OVER) {
            buttonStartNewGame.draw(batch);
            if (blinkTimer < blinkInterval / 2) messageGameOver.draw(batch);
            if (blinkTimer >= blinkInterval) blinkTimer = 0;
            if (currentColor  > targetColor) currentColor -= colorStep;
        } else {
            if (currentColor < 1f) currentColor += colorStep;
        }
        batch.setColor(currentColor, currentColor, currentColor, 1);
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft(), worldBounds.getTop() - 0.02f, Align.left);
        font.draw(batch, sbHP.append(HP).append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - 0.02f, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemiesEmitter.getLevel()), worldBounds.getRight(), worldBounds.getTop() - 0.02f, Align.right);
    }

    @Override
    protected void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (int i = 0; i < star.length; i++) {
            star[i].resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        music.dispose();
        laserSound.dispose();
        gameOverSound.dispose();
        font.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);
        if (state == State.GAME_OVER) buttonStartNewGame.touchDown(touch, pointer);

        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        mainShip.touchUp(touch, pointer);
        if (state == State.GAME_OVER) buttonStartNewGame.touchUp(touch, pointer);

        return super.touchUp(touch, pointer);
    }

    private void startNewGame() {
        state = State.PLAYING;
        gameOverSound.stop();
        music.play();
        mainShip.startNewGame();
        frags = 0;
        enemiesEmitter.setLevel(1);

        bulletPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
    }

    @Override
    public void actionPerformed(Object src) {
        if (src == buttonStartNewGame) {
            selected.play();
            startNewGame();
        }
    }

}

package ru.geekbrains.stargame.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.math.Rnd;
import ru.geekbrains.stargame.pool.EnemyPool;
import ru.geekbrains.stargame.sprites.Enemy;

public class EnemiesEmitter {

    private static float ENEMY_SMALL_HEIGHT = 0.1f;
    private static float ENEMY_SMALL_BULLET_HEIGHT = 0.02f;
    private static float ENEMY_SMALL_BULLET_VY = -0.3f;
    private static int ENEMY_SMALL_BULLET_DAMAGE = 1;
    private static float ENEMY_SMALL_RELOAD_INTERVAL = 3f;
    private static int ENEMY_SMALL_HP = 1;

    private static float ENEMY_MEDIUM_HEIGHT = 0.1f;
    private static float ENEMY_MEDIUM_BULLET_HEIGHT = 0.025f;
    private static float ENEMY_MEDIUM_BULLET_VY = -0.25f;
    private static int ENEMY_MEDIUM_BULLET_DAMAGE = 5;
    private static float ENEMY_MEDIUM_RELOAD_INTERVAL = 4f;
    private static int ENEMY_MEDIUM_HP = 5;

    private static float ENEMY_BIG_HEIGHT = 0.2f;
    private static float ENEMY_BIG_BULLET_HEIGHT = 0.04f;
    private static float ENEMY_BIG_BULLET_VY = -0.3f;
    private static int ENEMY_BIG_BULLET_DAMAGE = 10;
    private static float ENEMY_BIG_RELOAD_INTERVAL = 3f;
    private static int ENEMY_BIG_HP = 10;

    private final TextureRegion[] enemySmallRegion;
    private final TextureRegion[] enemyMediumRegion;
    private final TextureRegion[] enemyBigRegion;

    private final Vector2 enemySmallV = new Vector2(0f, -0.2f);
    private final Vector2 enemyMediumV = new Vector2(0f, -0.03f);
    private final Vector2 enemyBigV = new Vector2(0f, -0.005f);

    private final EnemyPool enemyPool;

    private Rect worldBounds;

    private TextureRegion bulletRegion;

    private float generateInterval = 4f;
    private float generateTimer;

    private int level = 1;
    private boolean leveledUp = false;

    public EnemiesEmitter(EnemyPool enemyPool, TextureAtlas atlas, Rect worldBounds) {
        this.enemyPool = enemyPool;
        this.worldBounds = worldBounds;
        TextureRegion textureRegion0 = atlas.findRegion("enemy0");
        this.enemySmallRegion = Regions.split(textureRegion0, 1, 2, 2);
        TextureRegion textureRegion1 = atlas.findRegion("enemy1");
        this.enemyMediumRegion = Regions.split(textureRegion1, 1, 2, 2);
        TextureRegion textureRegion2 = atlas.findRegion("enemy2");
        this.enemyBigRegion = Regions.split(textureRegion2, 1, 2, 2);

        this.bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generateEnemies(float delta, int frags) {
        int levelUpCriteria = frags / 6 + 1;
        if (level < levelUpCriteria) {
            level = levelUpCriteria;
            leveledUp = true;
        }

        generateTimer += delta * ((float) level / 4f + 1);
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            Enemy enemy = enemyPool.obtain();
            float type = (float) Math.random();
            if (type < 0.5f) {
                enemy.set(
                        enemySmallRegion,
                        enemySmallV,
                        bulletRegion,
                        ENEMY_SMALL_BULLET_HEIGHT,
                        ENEMY_SMALL_BULLET_VY,
                        ENEMY_SMALL_BULLET_DAMAGE * (level / 3 + 1),
                        ENEMY_SMALL_RELOAD_INTERVAL / ((float) level / 5f + 1),
                        ENEMY_SMALL_HEIGHT,
                        ENEMY_SMALL_HP,
                        worldBounds
                );
            } else if (type < 0.8f) {
                enemy.set(
                        enemyMediumRegion,
                        enemyMediumV,
                        bulletRegion,
                        ENEMY_MEDIUM_BULLET_HEIGHT,
                        ENEMY_MEDIUM_BULLET_VY,
                        ENEMY_MEDIUM_BULLET_DAMAGE * (level / 3 + 1),
                        ENEMY_MEDIUM_RELOAD_INTERVAL / ((float) level / 5f + 1),
                        ENEMY_MEDIUM_HEIGHT,
                        ENEMY_MEDIUM_HP,
                        worldBounds
                );
            } else {
                enemy.set(
                        enemyBigRegion,
                        enemyBigV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_VY,
                        ENEMY_BIG_BULLET_DAMAGE * (level / 3 + 1),
                        ENEMY_BIG_RELOAD_INTERVAL / ((float) level / 5f + 1),
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_HP,
                        worldBounds
                );
            }
            enemy.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemy.getHalfWidth(), worldBounds.getRight() - enemy.getHalfWidth());
            enemy.setBottom(worldBounds.getTop());
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isLeveledUp() {
        return leveledUp;
    }

    public void setLeveledUp(boolean leveledUp) {
        this.leveledUp = leveledUp;
    }
}

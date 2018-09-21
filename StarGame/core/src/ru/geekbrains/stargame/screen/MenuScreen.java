package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Base2DScreen;

public class MenuScreen extends Base2DScreen {

    SpriteBatch batch;
    Texture img;
    Texture ship;
    Vector2 pos;        //position
    Vector2 target;     //target to go
    Vector2 v;          //velocity

    private int maxShipAvailableX;
    private int maxShipAvailableY;
    private static final String LOG_TAG = "MenuScreen";

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        img = new Texture("background.jpg");
        ship = new Texture("spaceship.png");

        float centerPositionX = (Gdx.graphics.getWidth() - ship.getWidth()) / 2;
        pos = new Vector2(centerPositionX,  1f);
        v = new Vector2(0f, 0f);
        target = pos.cpy();

        getShipBorders(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        //Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        drawTexture(ship);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight() - screenY;
        screenX = screenX > maxShipAvailableX ? maxShipAvailableX : screenX;
        screenY = screenY > maxShipAvailableY ? maxShipAvailableY : screenY;
        target = new Vector2(screenX, screenY);
        v = target.cpy().sub(pos);
        v.nor().scl(3f);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        moveShipByArrows(keycode, 3);
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        moveShipByArrows(keycode, -3);
        return super.keyUp(keycode);
    }

    @Override
    public void resize(int width, int height) {
        getShipBorders(width, height);
        super.resize(width, height);
    }

    private void drawTexture(Texture texture) {
        boolean borderReachedX = pos.x > maxShipAvailableX || pos.x < 0;
        boolean borderReachedY = pos.y > maxShipAvailableY || pos.y < 0;

        int offset = 1;
        if (borderReachedX) {
            pos.x = pos.x > 0 ? pos.x = maxShipAvailableX - offset : offset;
        }
        if (borderReachedY) {
            pos.y = pos.y > 0 ? pos.y = maxShipAvailableY - offset : offset;
        }

        float catchRadius = 2f;
        if (pos.dst(target) < catchRadius) v.setLength(0);
        batch.draw(texture, pos.x, pos.y);
        pos.add(v);
    }

    private void getShipBorders(int width, int height) {
        maxShipAvailableX =  width - ship.getWidth();
        maxShipAvailableY =  height - ship.getHeight();
    }

    private void moveShipByArrows(int keycode, int velocity) {
        boolean isArrows = keycode >= 19 && keycode <= 22;
        if (!isArrows) return;
        target.set(-1f, -1f);

        final int up = 19;
        final int down = 20;
        final int left = 21;
        final int right = 22;

        switch (keycode) {
            case up:
                v.y += velocity;
                Gdx.app.log(LOG_TAG, "v.x = " + v.x + ", v.y = " +  v.y );
                break;
            case down:
                v.y -= velocity;
                Gdx.app.log(LOG_TAG, "v.x = " + v.x + ", v.y = " +  v.y );
                break;
            case left:
                v.x -= velocity;
                Gdx.app.log(LOG_TAG, "v.x = " + v.x + ", v.y = " +  v.y );
                break;
            case right:
                v.x += velocity;
                Gdx.app.log(LOG_TAG, "v.x = " + v.x + ", v.y = " +  v.y );
                break;
        }
    }

}

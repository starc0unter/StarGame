package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.Background;
import ru.geekbrains.stargame.Exit;
import ru.geekbrains.stargame.base.Base2DScreen;
import ru.geekbrains.stargame.math.Rect;


public class MenuScreen extends Base2DScreen {

    Background background;
    Exit exit;
    Texture backgroundTexture;
    Texture exitTexture;
    Vector2 pos;


    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        exitTexture = new Texture("exit.png");
        exit = new Exit(new TextureRegion(exitTexture));
        backgroundTexture = new Texture("bg.png");
        background = new Background(new TextureRegion(backgroundTexture));
        pos = new Vector2(0f,0f);
    }


    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1.0f, 0.7f, 1.0f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        exit.draw(batch);
        batch.end();
    }

    @Override
    protected void resize(Rect worldBounds) {
        background.resize(worldBounds);
        exit.resize(worldBounds);
    }

    @Override
    public void dispose() {
        exitTexture.dispose();
        backgroundTexture.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (exit.isMe(touch)) exit.touchDown(touch, pointer);
        return super.touchDown(touch, pointer);
    }
}

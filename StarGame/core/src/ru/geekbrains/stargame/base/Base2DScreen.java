package ru.geekbrains.stargame.base;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

public class Base2DScreen implements Screen, InputProcessor {

    protected Game game;
    private final String LOG_TAG = "Base2DScreen";

    public Base2DScreen(Game game) {
        this.game = game;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(LOG_TAG, "Window resized to the width of " + width + " and the height of " + height);
    }

    @Override
    public void pause() {
        Gdx.app.log(LOG_TAG, "Paused");
    }

    @Override
    public void resume() {
        Gdx.app.log(LOG_TAG, "Resumed");
    }

    @Override
    public void hide() {
        Gdx.app.log(LOG_TAG, "Hidden");
    }

    @Override
    public void dispose() {
        Gdx.app.log(LOG_TAG, "Disposed");
    }

    @Override
    public boolean keyDown(int keycode) {
        Gdx.app.log(LOG_TAG, "keyDown: " + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        Gdx.app.log(LOG_TAG, "keyUp: " + keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log(LOG_TAG, "touchDown at X = " + screenX + ", Y = " + screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log(LOG_TAG, "touchUp at X = " + screenX + ", Y = " + screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

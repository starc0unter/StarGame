package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.math.Rect;

public class HeroShip extends Sprite {

    private Vector2 v = new Vector2();
    private Rect worldBounds;
    private float moveVelocity = 1f;


    public HeroShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"));
        regions[0].setRegionWidth(regions[0].getRegionWidth() / 2);
        v.setLength(0);
        setHeightProportion(0.2f);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        checkAndHandleBounds();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float offset = 0.15f;
        setBottom((1 - offset) * worldBounds.getBottom());
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (touch.x > 0) v.add(moveVelocity, 0);
        else v.add(- moveVelocity, 0);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        v.setLength(0);
        return super.touchUp(touch, pointer);
    }

    public void keyDown(int keycode) {
        moveShipByArrows(keycode, moveVelocity);
    }

    public void keyUp(int keycode) {
        moveShipByArrows(keycode, - moveVelocity);
    }

    private void moveShipByArrows(int keycode, float moveVelocity) {
        switch (keycode) {
            case  Input.Keys.LEFT:
                v.x -= moveVelocity;
                break;
            case Input.Keys.RIGHT:
                v.x += moveVelocity;
                break;
        }
    }

    private void checkAndHandleBounds() {
        if (getLeft() < worldBounds.getLeft()) setLeft(worldBounds.getLeft());
        if (getRight() > worldBounds.getRight()) setRight(worldBounds.getRight());
    }

}

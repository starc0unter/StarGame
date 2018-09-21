package ru.geekbrains.stargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.math.Rect;

public class Exit extends Sprite {

    public Exit(TextureRegion region) {
        super(region);
    }

    @Override
    public void resize(Rect worldBounds) {
        float size = 0.1f;
        setHeightProportion(worldBounds.getHeight() * size);
        pos.set(worldBounds.getHalfWidth() - this.getHalfWidth(),
                worldBounds.getHalfHeight() - this.getHalfHeight());
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        Gdx.app.exit();
        return super.touchDown(touch, pointer); //useless line, but if we change logic in future, it can become useful somehow
    }
}

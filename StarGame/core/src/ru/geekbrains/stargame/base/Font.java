package ru.geekbrains.stargame.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Font extends BitmapFont {

    public Font(String fontFile, String imageFile) {
        super(Gdx.files.internal(fontFile), Gdx.files.internal(imageFile), false, false);
        getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.MipMapLinearLinear);
    }

    public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, int align) {
        return super.draw(batch, str, x, y, 0f, align, false);
    }

    public void setFontSize(float worldSize) {
        getData().setScale(worldSize / getCapHeight());
    }

}

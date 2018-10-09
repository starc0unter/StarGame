package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.base.ActionListener;
import ru.geekbrains.stargame.base.ScaledTouchUpButton;

public class ButtonStartNewGame extends ScaledTouchUpButton {

    public ButtonStartNewGame(TextureAtlas atlas, ActionListener actionListener) {
        super(atlas.findRegion("button_new_game"), actionListener, 0.9f);
        setHeightProportion(0.05f);
        setBottom(-0.055f);
    }

}

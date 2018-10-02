package ru.geekbrains.stargame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.geekbrains.stargame.Star2DGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		System.setProperty("user.name", "\\xD0\\xA7\\xD0\\xB5\\xD0\\xBD\\xD1\\x86\\xD0\\xBE\\xD0\\xB2");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		float aspect = 3f/4f;
		config.resizable = false;
		config.width = 300;
		config.height = (int) (config.width / aspect);
		new LwjglApplication(new Star2DGame(), config);
	}
}

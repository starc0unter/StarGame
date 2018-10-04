package ru.geekbrains.stargame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class StarGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	TextureRegion region;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		region = new TextureRegion(img, 100, 100, 150, 150);

		Vector2 v1 = new Vector2(1,3);
		Vector2 v2 = new Vector2(1,2);
		v1.add(v2);
		System.out.println("v1 x = " + v1.x + " v1 y = " + v1.y);

		v1.set(6,2);
		v2.set(2,3);
		v1.sub(v2);
		System.out.println("v1 x = " + v1.x + " v1 y = " + v1.y);

		System.out.println("v1 len = " + v1.len());
		v1.scl(0.5f);
		System.out.println("v1 x = " + v1.x + " v1 y = " + v1.y);
		System.out.println("v1 len = " + v1.len());

		System.out.println("v1.dot(v2) = " + v1.dot(v2));
		System.out.println("v1 len = " + v1.len());
		v1.cpy().nor();
		System.out.println("v1 x = " + v1.x + " v1 y = " + v1.y);
		System.out.println("v1 len = " + v1.len());

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0.4f, 0.6f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 100, 100);
		batch.draw(region, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}

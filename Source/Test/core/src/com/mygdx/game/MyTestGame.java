package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MyTestGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture ship;
	int shipX = 0;
	int shipY = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		ship = new Texture("raumschiff.png");
	}

	@Override
	public void render () {
		if(Gdx.input.isTouched()){
			shipX = Gdx.input.getX();
			shipY = Gdx.input.getY();
		}
		
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.draw(ship, shipX, shipY);
		batch.end();
	}
}

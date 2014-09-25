package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyTestGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture ship;
	int shipX = 0;
	int shipY = 0;
	ArrayList<Vector2> shipPosList = new ArrayList<Vector2>();
	TestObject movingObject;
	float state_time = 0f;
	
	@Override
	public void create () {
		OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();

		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		//img = new Texture("badlogic.jpg");
		ship = new Texture("raumschiff.png");
		
		movingObject = new TestObject(10, 10);
	}

	@Override
	public void render () {
		this.state_time += Gdx.graphics.getDeltaTime();
		this.movingObject.step();
		shipPosList.clear();
		for (int i = 0; i < 20; i++) {
			if(Gdx.input.isTouched(i)){
				Vector2 pos = new Vector2();
				pos.set(Gdx.input.getX(i), Gdx.input.getY(i));
				shipPosList.add(pos);
				this.movingObject.setY(pos.y);
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
			Gdx.app.exit();
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, 0, 0);
		batch.draw(
				this.movingObject.getAnimation().getKeyFrame(this.state_time, true), 
				this.movingObject.getX(), 
				this.movingObject.getY()
				);
		for (Vector2 pos : shipPosList) {
			batch.draw(ship, pos.x, pos.y + 100);
		}
		batch.end();
	}
}
package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class OldDemo implements Screen {
	MyTestGame game;
	SpriteBatch batch;
	Texture img;
	Texture ship;
	int shipX = 0;
	int shipY = 0;
	ArrayList<Vector2> shipPosList = new ArrayList<Vector2>();
	TestObject movingObject;
	float state_time = 0f;

	public OldDemo(MyTestGame game) {
		this.game = game;
		Gdx.input.setCatchBackKey(true);
		OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();

		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		//img = new Texture("badlogic.jpg");
		ship = new Texture("raumschiff.png");

		movingObject = new TestObject(10, 10);
	}

	public void exit(){
		game.setLastScreen();
	}

	@Override
	public void render (float delta) {
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) ||
				Gdx.input.isKeyPressed(Input.Keys.BACK))
		{
			this.exit();
		}
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

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}

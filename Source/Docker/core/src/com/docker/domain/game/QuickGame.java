package com.docker.domain.game;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.docker.domain.gameobject.Container;
import com.docker.domain.gameobject.Ship;
import com.docker.domain.gameobject.Train;

public class QuickGame extends AbstractGame {
	private static final double TIMELEFT = 60;

	private Stage stage;
	private ExtendViewport viewport;

	public QuickGame(Game application) {
		super(application);
		setTimeLeft(TIMELEFT);
		setShip(new Ship(40, 10, 400, 10f, 10f));
		setTrain(new Train(5, 0f, 680f));
		
		this.viewport = new ExtendViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		this.stage = new Stage(viewport);

		Gdx.input.setInputProcessor(this.stage);
		Gdx.input.setCatchBackKey(true);
		this.stage.addActor(getShip());
		this.stage.addActor(getTrain());
		getTrain().addContainer(new Container(3, 3, Color.YELLOW, 0, 0));
		getTrain().addContainer(new Container(3, 2, Color.RED, 0, 0));
		getTrain().addContainer(new Container(3, 1, Color.ORANGE, 0, 0));
		getTrain().addContainer(new Container(3, 4, Color.GREEN, 0, 0));
		getTrain().addContainer(new Container(3, 5, Color.BLUE, 0, 0));
		getShip().addContainer(61, new Container(2, 4, Color.GRAY, 0, 0));
		getShip().addContainer(98, new Container(2, 4, Color.DARK_GRAY, 0, 0));
		getShip().addContainer(65, new Container(3, 1, Color.YELLOW, 0, 0));
		getShip().addContainer(102, new Container(3, 1, Color.OLIVE, 0, 0));
		getShip().addContainer(98, new Container(3, 3, Color.ORANGE, 0, 0));
	}

	@Override
	public void render(float delta) {

		this.stage.act(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.draw();
		
		if(Gdx.input.justTouched()){
			Container container = new Container(3, 2, Color.GREEN, 0, 0);
			getShip().addContainer(Gdx.input.getX(), container);
		}
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
		// TODO Auto-generated method stub

	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public ExtendViewport getViewport() {
		return viewport;
	}

	public void setViewport(ExtendViewport viewport) {
		this.viewport = viewport;
	}

	
}

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
	private static final double GAME_DURATION = 60;

	private double timeLeft;

	private Stage stage;
	private ExtendViewport viewport;

	public QuickGame(Game application) {
		super(application);
		setTimeLeft(GAME_DURATION);
		setShip(new Ship(10, 4, 5, 10f, 10f));
		setTrain(new Train(5, 0f, 160f));
		
//		this.viewport = new ExtendViewport(WIDTH, HEIGHT);
		this.viewport = new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		this.stage = new Stage(viewport){
			 @Override
			   public boolean touchDown (int x, int y, int pointer, int button) {
				 	getShip().setPreviewContainer(x, getTrain().getFirstContainer());
				 	return true;
			   }
			 
			@Override
			   public boolean touchDragged (int x, int y, int pointer) {
				getShip().setPreviewContainer(x, getTrain().getFirstContainer());
			 	return true;
			   }

			   @Override
			   public boolean touchUp (int x, int y, int pointer, int button) {
				   if (getShip().addContainer(x, getTrain().getFirstContainer())) {
						getTrain().removeContainer();
						return true;
					}
				   return false;
			   }
			
		};

		Gdx.input.setInputProcessor(this.stage);
		Gdx.input.setCatchBackKey(true);
		this.stage.addActor(getShip());
		this.stage.addActor(getTrain());
		getTrain().addContainer(new Container(3, 3, Color.YELLOW, 0, 0));
		getTrain().addContainer(new Container(3, 2, Color.RED, 0, 0));
		getTrain().addContainer(new Container(3, 1, Color.ORANGE, 0, 0));
		getTrain().addContainer(new Container(3, 4, Color.GREEN, 0, 0));
		getTrain().addContainer(new Container(3, 5, Color.BLUE, 0, 0));
//		getShip().addContainer(61, new Container(2, 4, Color.GRAY, 0, 0));
//		getShip().addContainer(98, new Container(2, 4, Color.DARK_GRAY, 0, 0));
//		getShip().addContainer(65, new Container(3, 1, Color.YELLOW, 0, 0));
//		getShip().addContainer(102, new Container(3, 1, Color.OLIVE, 0, 0));
//		getShip().addContainer(98, new Container(3, 3, Color.ORANGE, 0, 0));
		
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		this.stage.act(Gdx.graphics.getDeltaTime());

//		if(Gdx.input.justTouched()){
//			if (getShip().addContainer(Gdx.input.getX(), getTrain().getFirstContainer())) {
//				Container container = getTrain().removeContainer();
//				getCrane().deployContainer(container, getShip(), 2);
//			}			
//		}
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.draw();
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

	public double getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(double timeLeft) {
		this.timeLeft = timeLeft;
	}

	
}

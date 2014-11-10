package com.docker.domain.game;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.docker.domain.gameobject.Background;
import com.docker.domain.gameobject.Container;
import com.docker.domain.gameobject.Foreground;
import com.docker.domain.gameobject.Ship;
import com.docker.domain.gameobject.Train;
import com.docker.technicalservices.WorldStage;

public class QuickGame extends AbstractGame {
	private static final double GAME_DURATION = 60;

	private double timeLeft;

	private WorldStage stage;
	private ExtendViewport viewport;

	public QuickGame(Game application) {
		super(application);
		setTimeLeft(GAME_DURATION);
		setShip(new Ship(10, 4, 5, 10f, 10f));
		setTrain(new Train(5, 0f, 160f));
		
		this.viewport = new ExtendViewport(WIDTH, HEIGHT);
//		this.viewport = new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		this.stage = new WorldStage(viewport){
			
			 @Override
			   public boolean touchDown (int x, int y, int pointer, int button) {
					Vector2 touchCoords = new Vector2(x,y);
				    touchCoords = getViewport().unproject(touchCoords);
				 	getShip().setPreviewContainer(touchCoords.x, getTrain().getFirstContainer());
				 	return true;
			   }
			 
			@Override
			   public boolean touchDragged (int x, int y, int pointer) {
				Vector2 touchCoords = new Vector2(x,y);
			    touchCoords = getViewport().unproject(touchCoords);
				getShip().setPreviewContainer(touchCoords.x, getTrain().getFirstContainer());
			 	return true;
			   }

			   @Override
			   public boolean touchUp (int x, int y, int pointer, int button) {
					Vector2 touchCoords = new Vector2(x,y);
				    touchCoords = getViewport().unproject(touchCoords);
				   if (getShip().addContainer(touchCoords.x, getTrain().getFirstContainer())) {
					   	getCrane().deployContainer(getTrain().removeContainer(), getShip(), touchCoords.x);
						return true;
					}
				   return false;
			   }
			
		};
		Background background = new Background(this.stage.getWidth(), this.stage.getHeight());
		background.toBack();
		this.stage.setBackground(background);
		Foreground foreground = new Foreground(this.stage.getWidth());
		foreground.toFront();
		this.stage.setForeground(foreground);
		this.getShip().setZIndex(50);
		
		this.stage.addActor(getShip());
		this.stage.addActor(getTrain());
		getTrain().addContainer(new Container(3, 3, Color.YELLOW));
		getTrain().addContainer(new Container(3, 2, Color.RED));
		getTrain().addContainer(new Container(3, 1, Color.ORANGE));
		getTrain().addContainer(new Container(3, 4, Color.GREEN));
		getTrain().addContainer(new Container(3, 5, Color.BLUE));
		
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
		Gdx.input.setInputProcessor(this.stage);
		Gdx.input.setCatchBackKey(true);
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

	public WorldStage getStage() {
		return stage;
	}

	public void setStage(WorldStage stage) {
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

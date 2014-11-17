package com.docker.domain.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.docker.domain.gameobject.Background;
import com.docker.domain.gameobject.Container;
import com.docker.domain.gameobject.Crane;
import com.docker.domain.gameobject.Foreground;
import com.docker.domain.gameobject.Ship;
import com.docker.domain.gameobject.Train;
import com.docker.technicalservices.WorldStage;

public class QuickGame extends AbstractGame {
	private static final double GAME_DURATION = 60;

	private double timeLeft;

	private WorldStage stage;
	private ExtendViewport viewport;
	private BitmapFont font;
	private boolean showDebugInfo = true;
	

	public QuickGame(Game application) {
		super(application);
		setTimeLeft(GAME_DURATION);
		setShip(new Ship(10, 4, 5, 10f, 10f));
		setTrain(new Train(15, 0f, HEIGHT-23));
		setCrane(new Crane(150, WIDTH/2, HEIGHT));
		setLoadRating(new LoadRating(3, 10, 1));
		
		this.font = new BitmapFont();
		font.setColor(Color.WHITE);
		this.viewport = new ExtendViewport(WIDTH, HEIGHT);
		this.stage = new WorldStage(viewport){

			@Override
			public boolean touchDown (int x, int y, int pointer, int button) {
				if(!getCrane().isDeploying() && getTrain().hasContainers()){
					Container container = getTrain().getFirstContainer();
					getShip().setPreviewContainer(getXPosition(x, y, container), container);
				}
				return true;
			}

			@Override
			public boolean touchDragged (int x, int y, int pointer) {
				if(!getCrane().isDeploying() && getTrain().hasContainers()){
					Container container = getTrain().getFirstContainer();
					getShip().setPreviewContainer(getXPosition(x, y, container),	container);
				}
				return true;
			}

			@Override
			public boolean touchUp (int x, int y, int pointer, int button) {
				if(!getCrane().isDeploying() &&getTrain().hasContainers()){
					Container container = getTrain().getFirstContainer();
					Vector2 realCoords = getShip().getRealCoord(getXPosition(x, y, container), container);
					if (realCoords != null) {
						getCrane().deployContainer(
								getTrain().removeContainer(),
								getShip(), 
								realCoords.x, 
								realCoords.y);
						return true;
					}
				}
				return false;
			}

			private float getXPosition(int x, int y, Container container) {
				Vector2 touchCoords = new Vector2(x,y);
				touchCoords = getViewport().unproject(touchCoords);
				float containerPos = getContainerPos(touchCoords.x, container);
				return containerPos;
			}

		};
		Background background = new Background(this.stage.getWidth(), this.stage.getHeight());
		background.toBack();
		this.stage.setBackground(background);
		Foreground foreground = new Foreground(this.stage.getWidth());
		foreground.toFront();
		this.stage.setForeground(foreground);
		this.getShip().setZIndex(50);

		Level level = Level.loadLevel();
		this.stage.addActor(level.getShip());
		this.stage.addActor(level.getTrain());
		this.stage.addActor(getCrane());
		
//		this.stage.addActor(getShip());
//		this.stage.addActor(getTrain());
//		this.stage.addActor(getCrane());
//		getTrain().addContainer(new Container(5, 1, Color.ORANGE));
//		getTrain().addContainer(new Container(5, 1, Color.ORANGE));
//		getTrain().addContainer(new Container(4, 4, Color.ORANGE));
//		getTrain().addContainer(new Container(3, 4, Color.GREEN));
//		getTrain().addContainer(new Container(1, 3, Color.YELLOW));
//		getTrain().addContainer(new Container(2, 2, Color.RED));
//		getTrain().addContainer(new Container(2, 2));
//		getTrain().addContainer(new Container(2, 2));
//		getTrain().addContainer(new Container(2, 2));
//		getTrain().addContainer(new Container(2, 2));
//		getTrain().addContainer(new Container(3, 1, Color.ORANGE));
//		getTrain().addContainer(new Container(4, 4, Color.GREEN));
//		getTrain().addContainer(new Container(5, 5, Color.BLUE));
//		getTrain().addContainer(new Container(6, 5));
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		this.stage.act(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.draw();

		getLoadRating().calculateScore(getShip().getGrid());
		if(showDebugInfo)
			drawDebugInfo(this.stage.getBatch());
		
		if(isGameOver() && !isScoreScreen()){
//			Add overlay Code here with Button and Score screen
			setScoreScreen(true);
		}
	}
	
	private void drawDebugInfo(Batch batch){
		float capsizeValue = getLoadRating().getCapsizeValue();
		
		batch.begin();
		this.font.setScale(0.7f	);
		if(Math.abs(capsizeValue) > 1)
			this.font.setColor(Color.RED);
        this.font.draw(batch, "CapsizeValue: "+capsizeValue, 20, HEIGHT-this.font.getCapHeight()-30);
		this.font.setColor(Color.WHITE);
        this.font.draw(batch, "Score: "+getLoadRating().getScore(), 20, HEIGHT-2*this.font.getCapHeight()-40);
		float[] breakvalues = getLoadRating().getBreakValues();
		for (int i = 0; i < getLoadRating().getLoadSums().length; i++) {
			this.font.draw(batch, 
					String.format("%.1f", getLoadRating().getLoadSums()[i]), 
					getShip().getX() + 30 + i*getShip().getElementWidth(), 
					getShip().getY()+35);
		}
		for (int i = 0; i < breakvalues.length; i++) {
			if(breakvalues[i] <= 0.5)
				this.font.setColor(0, 1, 0, 1);
			else if(breakvalues[i] <= 1)
				this.font.setColor(1, 1, 0, 1);
			else
				this.font.setColor(1, 0, 0, 1);
			this.font.draw(batch, 
					String.format("%.1f", breakvalues[i]), 
					getShip().getX() + 30 + i*getShip().getElementWidth(), 
					getShip().getY()+25);
		}
		this.font.setColor(Color.WHITE);
		batch.end();
	}

	public float getContainerPos(float fingerPos, Container container){
		return fingerPos - (container.getLength() / 2) * container.getElementWidth();
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

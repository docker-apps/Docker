package com.docker.domain.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.docker.Docker;
import com.docker.domain.gameobject.Background;
import com.docker.domain.gameobject.Container;
import com.docker.domain.gameobject.Crane;
import com.docker.domain.gameobject.Foreground;
import com.docker.domain.gameobject.Ship;
import com.docker.domain.gameobject.Train;
import com.docker.technicalservices.WorldStage;
//import com.docker.ui.menus.InGameMenu;
//import com.docker.ui.menus.ScoreScreen;

public abstract class AbstractGame extends ScreenAdapter {

	protected Docker application;
	protected WorldStage stage;
	protected ExtendViewport viewport;
	protected BitmapFont font;
	protected boolean showDebugInfo = true;	

	protected int score;
	protected double time;
	protected int lives;
	protected Ship ship;
	protected Train train;
	protected Crane crane;
	protected LoadRating loadRating;
	protected boolean gameOver;
	protected boolean scoreScreen;
	protected Music backgroundMusic;
	private Foreground foreground;
	private Background background;

	public AbstractGame(Docker application){
		this.application = application;

		this.score = 0;
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("hustle.mp3"));
		backgroundMusic.setLooping(true);
		backgroundMusic.setVolume(1);

		this.font = new BitmapFont();

		this.viewport = new ExtendViewport(Docker.WIDTH, Docker.HEIGHT);
		this.stage = new WorldStage(viewport){

			@Override
			public boolean touchDown (int x, int y, int pointer, int button) {
				boolean result = super.touchDown(x, y, pointer, button);
				if(!result){
					result = touchDownEvent(x, y, pointer, button);
				}
				return result;
			}

			@Override
			public boolean touchDragged (int x, int y, int pointer) {
				boolean result = super.touchDragged(x, y, pointer);
				if(!result){
					result = touchDraggedEvent(x, y, pointer);
				}
				return result;
			}

			@Override
			public boolean touchUp (int x, int y, int pointer, int button) {
				boolean result = super.touchUp(x, y, pointer, button);
				if(!result){
					result = touchUpEvent(x, y, pointer, button);
				}
				return result;
			}
		};		
		background = new Background(this.stage.getWidth(), this.stage.getHeight());
		background.toBack();
		this.stage.setBackground(background);
		foreground = new Foreground(this.stage.getWidth());
		foreground.toFront();
		this.stage.setForeground(foreground);
		
		// crane is basically the same in every game. Remove from abstraction if cranespeed becomes configurable.
        setCrane(new Crane(80, Docker.WIDTH/2, Docker.HEIGHT));
	}

	public boolean touchDownEvent(int x, int y, int pointer, int button){
		if(canPlayerAct())
			previewPosition(x, y);
		return true;
	}

	public boolean touchDraggedEvent(int x, int y, int pointer){
		if(canPlayerAct())
			previewPosition(x, y);
		return true;
	}

	public boolean touchUpEvent (int x, int y, int pointer, int button) {
		if(canPlayerAct())
			deployContainer(x, y);
		return true;
	}

	public boolean canPlayerAct(){
		return !getCrane().isDeploying() && getTrain().hasContainers();
	}

	public void previewPosition(int x, int y){
		Container container = getTrain().getFirstContainer();
		getShip().setPreviewContainer(getXPosition(x, y, container), container);
	}

	public void deployContainer(int x, int y){
		Container container = getTrain().getFirstContainer();
		Vector2 realCoords = getShip().getRealCoord(getXPosition(x, y, container), container);
		if (realCoords != null) {
			getCrane().deployContainer(
					getTrain().removeContainer(),
					getShip(), 
					realCoords.x, 
					realCoords.y);
		}
	}

	protected float getXPosition(int x, int y, Container container) {
		Vector2 touchCoords = new Vector2(x,y);
		touchCoords = getViewport().unproject(touchCoords);
		float containerPos = getContainerPos(touchCoords.x, container);
		return containerPos;
	}

	protected float getContainerPos(float fingerPos, Container container){
		return fingerPos - (container.getLength() / 2) * container.getElementWidth();
	}

	protected void drawDebugInfo(Batch batch){
		float capsizeValue = getLoadRating().getCapsizeValue();

		batch.begin();
		this.font.setScale(0.7f	);
		if(Math.abs(capsizeValue) > 1)
			this.font.setColor(Color.RED);
		this.font.draw(batch, "CapsizeValue: "+capsizeValue, 20, Docker.HEIGHT-this.font.getCapHeight()-30);
		this.font.setColor(Color.WHITE);
		this.font.draw(batch, "Score: "+getLoadRating().getScore(), 20, Docker.HEIGHT-2*this.font.getCapHeight()-40);
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

	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) ||
				Gdx.input.isKeyPressed(Input.Keys.BACK))
		{
			//application.setScreen(new InGameMenu(application));
		}
		
		this.time += delta;

		this.stage.act(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.draw();

		getLoadRating().calculateScore(getShip().getGrid());
		getShip().setBreakValues(getLoadRating().getBreakValues());
		this.foreground.setCapsizeValue(getLoadRating().getCapsizeValue());
		if((!train.hasContainers() && !getCrane().isDeploying())|| lives <= 0 ){
			gameOver();
		}
		if(showDebugInfo)
			drawDebugInfo(this.stage.getBatch());
		
		
		
		
	}

	@Override
	public void show(){
		backgroundMusic.play();
		Gdx.input.setInputProcessor(this.stage);
		Gdx.input.setCatchBackKey(true);
	}
	@Override
	public void dispose() {
		this.backgroundMusic.dispose();
		this.stage.dispose();
		this.font.dispose();
	}

	public int removeLive() {
		this.lives--;
		return this.lives;
	}
	
	public void gameOver(){
		getLoadRating().calculateScore(getShip().getGrid());
		//application.setScreen(new ScoreScreen(application, getLoadRating().getScore()));
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
		this.stage.addActor(ship);
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
		this.stage.addActor(train);
	}

	public Crane getCrane() {
		return crane;
	}

	public void setCrane(Crane crane) {
		this.crane = crane;
		this.stage.addActor(crane);
	}

	public LoadRating getLoadRating() {
		return loadRating;
	}

	public void setLoadRating(LoadRating loadRating) {
		this.loadRating = loadRating;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public boolean isScoreScreen() {
		return scoreScreen;
	}

	public void setScoreScreen(boolean scoreScreen) {
		this.scoreScreen = scoreScreen;
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

	public boolean isShowDebugInfo() {
		return showDebugInfo;
	}

	public void setShowDebugInfo(boolean showDebugInfo) {
		this.showDebugInfo = showDebugInfo;
	}
}

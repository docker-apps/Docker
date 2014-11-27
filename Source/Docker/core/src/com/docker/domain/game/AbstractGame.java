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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.docker.Docker;
import com.docker.domain.gameobject.Background;
import com.docker.domain.gameobject.Container;
import com.docker.domain.gameobject.Crane;
import com.docker.domain.gameobject.Foreground;
import com.docker.domain.gameobject.Ship;
import com.docker.domain.gameobject.Train;
import com.docker.technicalservices.Persistence;
import com.docker.technicalservices.WorldStage;
import com.docker.ui.menus.PauseMenu;
import com.docker.ui.menus.ScoreScreen;

public abstract class AbstractGame extends ScreenAdapter {

	protected Docker application;
	protected WorldStage stage;
	protected ExtendViewport viewport;
	protected BitmapFont font;
	protected boolean showDebugInfo = false;	

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
	protected Foreground foreground;
	protected Background background;

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

	/**
	 * This method gets called on any unhandled touchDown Event on the screen.
	 * 
	 * @param x the x-Position of the touch event
	 * @param y the y-Position of the touch event
	 * @param pointer the pointer for the event.
	 * @param button the button, if one was pressed
	 * @return
	 */
	public boolean touchDownEvent(int x, int y, int pointer, int button){
		if(canPlayerAct())
			previewPosition(x, y);
		return true;
	}

	/**
	 * This method gets called on any unhandled touchDragged event on the screen.
	 * 
	 * @param x the x-Position of the touch event
	 * @param y the y-Position of the touch event
	 * @param pointer the pointer for the event.
	 * @return
	 */
	public boolean touchDraggedEvent(int x, int y, int pointer){
		if(canPlayerAct())
			previewPosition(x, y);
		return true;
	}

	/**
	 * This method gets called on any unhandled touchUp Event on the screen.
	 * 
	 * @param x the x-Position of the touch event
	 * @param y the y-Position of the touch event
	 * @param pointer the pointer for the event.
	 * @param button the button, if one was pressed
	 * @return
	 */
	public boolean touchUpEvent (int x, int y, int pointer, int button) {
		if(canPlayerAct())
			deployContainer(x, y);
		return true;
	}

	/**
	 * @return true if the player is allowed to do something (i.e. position a container)
	 */
	public boolean canPlayerAct(){
		return !getCrane().isDeploying() && getTrain().hasContainers();
	}

	/**
	 * Display a preview container on the ship.
	 * 
	 * @param x the x-position for the preview container
	 * @param y the y-position for the preview container
	 */
	public void previewPosition(int x, int y){
		Container container = getTrain().getFirstContainer();
		getShip().setPreviewContainer(getXPosition(x, y, container), container);
	}

	/**
	 * Deploy a container on the ship, to the designated coordinates.
	 * 
	 * @param x the x-Position to which the container should be deployed
	 * @param y the y-Position to which the container should be deployed
	 */
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

	/**
	 * Draw debug Infos, such as the current Loadrating values.
	 * 
	 * @param batch
	 */
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
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) ||
				Gdx.input.isKeyJustPressed(Input.Keys.BACK))
		{
			displayPauseScreen();
		}
		if(Gdx.input.isKeyJustPressed(Keys.D)){		//this is just for us
			this.showDebugInfo = !this.showDebugInfo;
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

	private void displayPauseScreen() {
		TextureRegion screenCap = ScreenUtils.getFrameBufferTexture();			
		application.setScreen(new PauseMenu(application, screenCap));
	}
	
	@Override
	public void pause(){
		displayPauseScreen();
	}

	@Override
	public void show(){
        if (Persistence.isMusicOn()) {
            backgroundMusic.setVolume(Persistence.getVolume());
            backgroundMusic.play();
        }
        if (!Persistence.isSoundOn()) {
            getShip().playContainerSound(false);
        }
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
		TextureRegion screenCap = ScreenUtils.getFrameBufferTexture();
		application.setScreen(new ScoreScreen(application, screenCap, getLoadRating().getScore()));
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
		this.foreground.setRemainingLives(lives);
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

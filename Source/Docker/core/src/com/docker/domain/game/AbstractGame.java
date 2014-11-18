package com.docker.domain.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.docker.Docker;
import com.docker.domain.gameobject.Crane;
import com.docker.domain.gameobject.Ship;
import com.docker.domain.gameobject.Train;

public abstract class AbstractGame extends ScreenAdapter {
	
	private Docker application;
	private int score;
	private double time;
	private int lives;
	private Ship ship;
	private Train train;
	private Crane crane;
	private LoadRating loadRating;
	private boolean gameOver;
	private boolean scoreScreen;
	
	
	

	public AbstractGame(Docker application){
		this.application = application;

		this.score = 0;
	}

	@Override
	public void render(float delta) {
		this.time += delta;
	}
	
	public int removeLive() {
		this.lives--;
		return this.lives;
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
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public Crane getCrane() {
		return crane;
	}

	public void setCrane(Crane crane) {
		this.crane = crane;
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
	
	
}

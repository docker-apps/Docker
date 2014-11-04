package com.docker.domain.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.docker.domain.gameobject.Crane;
import com.docker.domain.gameobject.Ship;
import com.docker.domain.gameobject.Train;

public abstract class AbstractGame extends ScreenAdapter {
	private Game application;
	private int score;
	private double timeLeft;
	private int lives;
	private Ship ship;
	private Train train;
	private Crane crane;
	
	public AbstractGame(Game application){
		this.application = application;

		this.score = 0;
	}
	

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public double getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(double timeLeft) {
		this.timeLeft = timeLeft;
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
}

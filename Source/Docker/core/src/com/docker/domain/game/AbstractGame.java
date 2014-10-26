package com.docker.domain.game;

import com.badlogic.gdx.ScreenAdapter;
import com.docker.domain.gameobject.Crane;
import com.docker.domain.gameobject.Ship;
import com.docker.domain.gameobject.Train;

public abstract class AbstractGame extends ScreenAdapter {
	private AbstractGame application;
	private int score;
	private double timeLeft;
	private int lives;
	private Ship ship;
	private Train train;
	private Crane crane;
	
	public AbstractGame(AbstractGame application){
		this.application = application;
		this.score = 0;
	}
}

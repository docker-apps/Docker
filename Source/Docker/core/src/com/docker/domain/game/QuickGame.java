package com.docker.domain.game;

import com.docker.Docker;

public class QuickGame extends AbstractGame {
	private static final double GAME_DURATION = 60;

	private double timeLeft;

	public QuickGame(Docker application) {
        super(application);
		setTimeLeft(GAME_DURATION);
		
		Level level = Level.loadLevel();
		
		setShip(level.getShip());
		setTrain(level.getTrain());
		setLives(level.getLifeCount());
		setLoadRating(new LoadRating(getShip().getBreakThreshold(), getShip().getCapsizeThreshold(), 1));
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		
		if(isGameOver() && !isScoreScreen()){
//			Add overlay Code here with Button and Score screen
			setScoreScreen(true);
		}
	}

	public double getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(double timeLeft) {
		this.timeLeft = timeLeft;
	}
	
}

package com.docker.domain.game;

import com.docker.Docker;
import com.docker.technicalservices.Persistence;

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
    public Integer endGame(Integer gameScore) {
        Integer highscore = Persistence.getQuickHighscore();
        if (highscore < gameScore) {
            Persistence.setQuickHighscore(gameScore);
            return gameScore;
        }
        return highscore;
    }

    @Override
    public void startNewGame() {
        application.setScreen(new QuickGame(application));
    }

    public double getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(double timeLeft) {
		this.timeLeft = timeLeft;
	}
	
}

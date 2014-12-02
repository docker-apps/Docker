package com.docker.domain.game;

import com.docker.Docker;
import com.docker.technicalservices.Persistence;

public class CareerGame extends AbstractGame {

    private String levelId;

	public CareerGame(Docker application, String levelId) {
        super(application);
        this.levelId = levelId;

		Level level = Level.loadLevel(levelId);
		this.setLives(level.getLifeCount());
		this.setShip(level.getShip());
		this.setTrain(level.getTrain());
		this.setLives(level.getLifeCount());
		setLoadRating(new LoadRating(ship.getBreakThreshold(), ship.getCapsizeThreshold(), 1));
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}

    @Override
    public Integer endGame(Integer gameScore) {
        int level = Integer.parseInt(levelId);
        Persistence.unlockLevel(String.valueOf(level + 1));
        Integer levelHighScore = Persistence.getLevelScore(levelId);
        if (gameScore > levelHighScore) {
            Persistence.setLevelScore(levelId, gameScore);
            return gameScore;
        }
        return levelHighScore;
    }
}

package com.docker.domain.game;

import com.docker.Docker;

public class CareerGame extends AbstractGame {

	public CareerGame(Docker application, String levelId) {
        super(application);

		Level level = Level.loadLevel(levelId);
		this.setLives(level.getLifeCount());
		this.setShip(level.getShip());
		this.setTrain(level.getTrain());
		setLoadRating(new LoadRating(ship.getBreakThreshold(), ship.getCapsizeThreshold(), 1));
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}
}

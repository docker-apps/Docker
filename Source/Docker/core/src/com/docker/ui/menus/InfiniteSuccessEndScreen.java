package com.docker.ui.menus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.docker.Docker;
import com.docker.domain.game.InfiniteGame;

/**
 * @author HAL9001
 * 
 * The Endscreen for the Infinite Game
 * 
 * As the Infinite Game cannot be lost per se, there is no Failure End Screen for the Infinity Game.
 *
 */
public class InfiniteSuccessEndScreen extends SuccessEndScreen{

	public InfiniteSuccessEndScreen(Docker application,
			TextureRegion background, InfiniteGame game, int gameScore,
			int highscore) {
		super(application, background, game, gameScore, highscore);
		game.getScore();
		
		text.setText(
				"Loaded Ships: " + game.getShipCounter() +
				"\nYour Score: " + gameScore +
				"\nHighscore: " + highscore);
	}

}

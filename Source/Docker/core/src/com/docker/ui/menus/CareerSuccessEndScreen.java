package com.docker.ui.menus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.docker.Docker;
import com.docker.domain.game.AbstractGame;
import com.docker.domain.game.CareerGame;
import com.docker.technicalservices.Persistence;
import com.docker.technicalservices.Resource;
import com.docker.technicalservices.SoundHandler;

public class CareerSuccessEndScreen extends SuccessEndScreen {

	private Button nextLevelButton = createNextLevelButton(skin);

	public CareerSuccessEndScreen(final Docker application, TextureRegion background,
			final AbstractGame game, int gameScore, int highscore) {
		super(application, background, game, gameScore, highscore);

		nextLevelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundHandler.stopMusic(Resource.getEndScreenTheme());
				CareerGame g = (CareerGame) game;
				int level = Integer.parseInt( g.getLevelId());
				String nextLevel = String.valueOf(level + 1);
				if (Persistence.hasLevel(nextLevel)) {
					application.setScreen(new CareerGame(application, nextLevel));
				}
			}
		});

		//how do I determine whether the level was played for the first time ort not?
		//		text.setText(
		//				"You unlocked a new Level!\n" + 
		//				"Your Score: " + gameScore +
		//				"\nHighscore: " + highscore
		//				);

		table.add(nextLevelButton).size(100, 40).padTop(10).padLeft(5).center();
		
		Image iconImage = new Image(Resource.getDockerSkinTextureAtlas().findRegion("career_menu_icon"));
		homeButton.clear();
    	homeButton.add(iconImage);
    	homeButton.addListener(new ClickListener(){
    		@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundHandler.stopMusic(Resource.getEndScreenTheme());
				application.returnToCareerScreen();
			}
    	});
	}
}

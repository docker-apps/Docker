package com.docker.ui.menus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.game.AbstractGame;

public class SuccessEndScreen extends AbstractMenu{

	protected Label title = new Label("Success!", skin, "title-white");
	protected Label text;
	protected Button homeButton = createHomeButton(skin);
	protected Button retryButton = createRetryButton(skin);

	public SuccessEndScreen(final Docker application, TextureRegion background, final AbstractGame game, int gameScore, int highscore) {
		super(application, background);

		homeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				application.showAds(true);
				application.returnToMenu();
			}
		});
		retryButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.startNewGame();
			}
		});

		text = new Label(
				"Your Score: " + gameScore +
				"\nHighscore: " + highscore, 
				skin);

		table.add(title).padBottom(10).colspan(3).left().row();
		table.add(text).colspan(3).left().row();
		table.add(homeButton).size(100, 40).padTop(10).center();
		table.add(retryButton).size(100, 40).padTop(10).padLeft(5).center();
	}

}

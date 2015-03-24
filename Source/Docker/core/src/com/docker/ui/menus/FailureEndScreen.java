package com.docker.ui.menus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.game.AbstractGame;

public class FailureEndScreen extends AbstractMenu{

	protected Label title = new Label("Game Over!", skin, "title-white");
	protected Label text;
	protected Button homeButton = createHomeButton(skin);
	protected Button retryButton = createRetryButton(skin);

	public FailureEndScreen(final Docker application, TextureRegion background, final AbstractGame game) {
		super(application, background);


		homeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				application.returnToMenu();
			}
		});
		retryButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.startNewGame();
			}
		});

		if(game.getLives() <= 0){
			text = new Label("You lost all lives.\nNext time, try to plan ahead.", skin);
		}
		else if(game.shipIsSunk()){
			text = new Label("Your ship has sunk.\nTry to achieve better balance.", skin);
		}
		else if(game.shipIsBroken()){
			text = new Label("Your ship has broken.\nMind the weight distribution.", skin);
		}
		else{
			text = new Label("", skin);
		}

		table.add(title).padBottom(10).colspan(2).left().row();
		table.add(text).colspan(2).left().row();
		table.add(homeButton).size(100, 40).padTop(10).center();
		table.add(retryButton).size(100, 40).padTop(10).center();

	}
}

package com.docker.ui.menus;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.docker.Docker;
import com.docker.domain.game.AbstractGame;
import com.docker.domain.game.TutorialGame;

/**
 * Screen to be displayed when a running game is paused. *
 */
public class PauseMenu extends AbstractMenu {    
	private Button homeButton = createHomeButton(skin);
	private Button resumeButton = createResumeButton(skin);
	private Button retryButton = createRetryButton(skin);
	private Label title = new Label("Pause",skin, "title-white");


	public PauseMenu(final Docker application, TextureRegion background, final AbstractGame game) {
		this(application, background, game, false);
	}

	/**
	 * @param application A reference to the Docker Application (Game) object.
	 * @param background the background to be displayed. Should be a snapshot of the game.
	 */
	public PauseMenu(final Docker application, TextureRegion background, final AbstractGame game, final boolean isCareerMode) {
		super(application, background);

		if(isCareerMode)
			homeButton = createIconButton(skin, "career_menu_icon");
		else
			homeButton = createHomeButton(skin);

		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				application.showAds(false);
				application.returnToLastScreen();
			}
		});
		homeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				application.showAds(true);
				if(isCareerMode)
					application.returnToCareerScreen();
				else
					application.returnToMenu();
			}
		});
		retryButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.startNewGame();
			}
		});

		table.add(title).left().padBottom(15).row();
		table.add(resumeButton).size(100, 40).padTop(10).padLeft(5).center().uniform();
		if (!(game instanceof TutorialGame)) {
			table.add(retryButton).size(100, 40).padTop(10).padLeft(5).center().uniform();
		}
		table.add(homeButton).size(100, 40).padTop(10).padLeft(5).center().uniform();
		stage.addActor(table);
	}
}

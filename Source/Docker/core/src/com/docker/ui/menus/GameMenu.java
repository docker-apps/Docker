package com.docker.ui.menus;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.docker.Docker;
import com.docker.domain.game.InfiniteGame;
import com.docker.domain.game.QuickGame;
import com.docker.technicalservices.Resource;
import com.docker.technicalservices.SoundHandler;

/**
 * In this menu the player can choose which game he wants to play
 */
public class GameMenu extends AbstractMenu {
	private Label title = new Label("Game Modes",skin, "title");
	private TextButton careerGameButton = new TextButton("Career Game", skin);
	private TextButton quickGameButton = new TextButton("Quick Game", skin);
	private TextButton infiniteGameButton = new TextButton("Infinite Game", skin);
	private Button backButton = createBackButton(skin);


	/**
	 * @param application A reference to the Docker Application (Game) object.
	 */
	public GameMenu(final Docker application) {
		super(application);

		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				application.returnToLastScreen();
			}
		});
		careerGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				openNewMenu(new CareerMenu(application));
			}
		});
		quickGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
		        SoundHandler.stopMusic(Resource.getMenuTheme());
				application.showAds(false);
				application.setScreen(new QuickGame(application));
			}
		});
		infiniteGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
		        SoundHandler.stopMusic(Resource.getMenuTheme());
				application.showAds(false);
				application.setScreen(new InfiniteGame(application));
			}
		});
		table.add(title).center().colspan(2).padBottom(10).row();
		table.add(careerGameButton).size(160, 35).center().padBottom(5).row();
		table.add(quickGameButton).size(160, 35).center().padBottom(5).row();
		table.add(infiniteGameButton).size(160, 35).center().padBottom(5).row();
		table.add(backButton).size(100, 30).center();
	}
}

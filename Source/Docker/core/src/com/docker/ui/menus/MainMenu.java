package com.docker.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.technicalservices.Resource;

/**
 * the main menu
 */
public class MainMenu extends AbstractMenu{
	private final static float TITLE_SPACING = 5f;

	private TextButton gameMenuButton = new TextButton("Play", skin),
			settingsButton = new TextButton("Settings", skin),
			statisticsButton = new TextButton("Statistics", skin),
			premiumButton = new TextButton("Get Premium!", skin);

	/**
	 * @param application A reference to the Docker Application (Game) object.
	 */
	public MainMenu(final Docker application){
		super(application);
		this.setBackground(new MenuBackground(this.stage.getWidth(), this.stage.getHeight(), application.getInventory().hasPremium()));

		TextureRegion titleRegion = Resource.getDockerSkinTextureAtlas().findRegion("docker_title");
		TextureRegion logoRegion = Resource.getDockerSkinTextureAtlas().findRegion(
				application.getInventory().hasPremium() ? "docker_title_logo_gold" : "docker_title_logo");

		Image titleImage = new Image(titleRegion);
		titleImage.setSize(titleRegion.getRegionWidth(), titleRegion.getRegionHeight());		
		Image logoImage = new Image(logoRegion);
		logoImage.setSize(logoRegion.getRegionWidth(), logoRegion.getRegionHeight());

		titleImage.setPosition(
				(this.stage.getWidth()-titleImage.getWidth()-logoImage.getWidth() - TITLE_SPACING)/2, 
				(this.stage.getHeight()-titleImage.getHeight()-8f));
		logoImage.setPosition(
				(this.stage.getWidth()-titleImage.getWidth()-logoImage.getWidth() - TITLE_SPACING)/2 + titleImage.getWidth(), 
				(this.stage.getHeight()-titleImage.getHeight()-8f));

		Group titleGroup = new Group();
		titleGroup.addActor(titleImage);
		titleGroup.addActor(logoImage);
		this.stage.addActor(titleGroup);

		settingsButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				openNewMenu(new SettingsMenu(application));
			}
		});
		statisticsButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				openNewMenu(new StatisticsMenu(application));
			}
		});
		gameMenuButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				openNewMenu(new GameMenu(application));
			}
		});
		premiumButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				openNewMenu(new PremiumMenu(application));
			}
		});

		table.add(gameMenuButton).size(100, 35).padTop(20).padBottom(5).row();
		table.add(settingsButton).size(100, 35).padBottom(5).row();
		table.add(statisticsButton).size(100, 35).padBottom(5).row();
		table.add(premiumButton).size(100, 35).padBottom(5).row();
	}

	@Override
	public void handleInput(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
				Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
			Gdx.app.exit();
		}
	}
}

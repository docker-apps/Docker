package com.docker.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;

/**
 * the main menu
 */
public class MainMenu extends AbstractMenu{

    private TextButton gameMenuButton = new TextButton("Play", skin),
            settingsButton = new TextButton("Settings", skin),
            statisticsButton = new TextButton("Statistics", skin);
	
	public MainMenu(final Docker application){
		super(application);
		this.setBackground(new MenuBackground(this.stage.getWidth(), this.stage.getHeight()));
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/dockerskin.atlas"));
		TextureRegion titleRegion = atlas.findRegion("docker_title");
		Image titleImage = new Image(titleRegion);
		titleImage.setSize(titleRegion.getRegionWidth(), titleRegion.getRegionHeight());
		titleImage.setPosition(
				(this.stage.getWidth()-titleImage.getWidth())/2, 
				(this.stage.getHeight()-titleImage.getHeight()-8f));
		this.stage.addActor(titleImage);
		
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
        
        table.add(gameMenuButton).size(100, 35).padTop(20).padBottom(5).row();
        table.add(settingsButton).size(100, 35).padBottom(5).row();
        table.add(statisticsButton).size(100, 35).padBottom(5).row();
	}
	
	@Override
    public void handleInput(){
    	if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            Gdx.app.exit();
        }
    }
}

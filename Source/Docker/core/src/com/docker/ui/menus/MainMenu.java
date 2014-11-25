package com.docker.ui.menus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.docker.Docker;

public class MainMenu extends AbstractMenu{

    private TextButton gameMenuButton = new TextButton("Play", skin),
            settingsButton = new TextButton("Settings", skin),
            statisticsButton = new TextButton("Statistics", skin);
    private Label title = new Label("Docker",skin);
	
	public MainMenu(final Docker application){
		super(application);
		
        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new SettingsMenu(application));
            }
        });
        statisticsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new StatisticsMenu(application));
            }
        });
        gameMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameMenu(application));
            }
        });

        table.add(title).padBottom(10).row();
        table.add(gameMenuButton).size(100, 35).padBottom(5).row();
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

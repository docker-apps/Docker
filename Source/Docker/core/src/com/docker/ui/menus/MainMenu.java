package com.docker.ui.menus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;

public class MainMenu implements Screen{
	Docker application;

    private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

    private Stage stage = new Stage();
    private Table table = new Table();

    private TextButton gameMenuButton = new TextButton("Play", skin),
            settingsButton = new TextButton("Settings", skin),
            statisticsButton = new TextButton("Statistics", skin);
    private Label title = new Label("Docker",skin);
	
	public MainMenu(final Docker application){
		this.application = application;


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

        table.add(title).padBottom(40).row();
        table.add(gameMenuButton).size(150, 60).padBottom(20).row();
        table.add(settingsButton).size(150, 60).padBottom(20).row();
        table.add(statisticsButton).size(150, 60).padBottom(20).row();

        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(false);
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(false);

    }

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
        stage.dispose();
        skin.dispose();
	}
}

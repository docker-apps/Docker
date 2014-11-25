package com.docker.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.docker.Docker;

public class AbstractMenu implements Screen {
	Docker application;
	
    protected Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

    protected Stage stage;
    protected ExtendViewport viewport;
    protected Table table;
    
    public AbstractMenu(final Docker application){
    	this.application = application;
		
		this.viewport = new ExtendViewport(Docker.WIDTH, Docker.HEIGHT);
		this.stage = new Stage(viewport);
		this.table = new Table();
		
        table.setFillParent(true);
        stage.addActor(table);
    }
    
    public void handleInput(){
    	lastScreenOnReturn();
    }
    
    public void lastScreenOnReturn(){
    	if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            application.setLastScreen();
        }
    }

    @Override
	public void render(float delta) {
    	handleInput();
    	
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
        Gdx.input.setCatchBackKey(true);
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

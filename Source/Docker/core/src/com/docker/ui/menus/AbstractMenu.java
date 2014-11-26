package com.docker.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.docker.Docker;
import com.docker.technicalservices.WorldStage;

/** 
 * Abstracton for all Menus. Sets up a stage and a table as well as the basic input handling and rendering.
 * 
 * @author HAL9000
 *
 */
public class AbstractMenu implements Screen {
	Docker application;
	
    protected Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

    protected WorldStage stage;
    protected ExtendViewport viewport;
    protected Table table;
    
    public AbstractMenu(final Docker application){
    	this.application = application;
		
		this.viewport = new ExtendViewport(Docker.WIDTH, Docker.HEIGHT);
		this.stage = new WorldStage(viewport);
		this.table = new Table();
		
        table.setFillParent(true);
        stage.addActor(table);
    }
    
    public AbstractMenu(final Docker application, TextureRegion background){
    	this(application);
    	this.setBackground(background);
    }
    
    /**
     * Hook-Operation which will be called every render cycle. Override to individually handle input.
     * 
     * Calls lastScreenOnReturn() by default
     */
    public void handleInput(){
    	lastScreenOnReturn();
    }
    
    /**
     * Returns to the last screen on Escape or Return-Key
     */
    public void lastScreenOnReturn(){
    	if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            application.setLastScreen();
        }
    }
    
    /**
     * Sets the Background to be displayed in the Menu
     * 
     * @param texture the texture to be displayed as a background.
     */
    public void setBackground(TextureRegion texture){
        Image bg = new Image(texture);
        bg.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        bg.setPosition(0f, 0f);
        bg.setColor(1f, 1f, 1f, 0.5f);
        this.stage.setBackground(bg);
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

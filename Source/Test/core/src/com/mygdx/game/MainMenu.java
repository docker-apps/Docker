package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen{
	private MyTestGame game;
	private SpriteBatch batch;
	private Skin skin;
	private Stage stage;

	public MainMenu(MyTestGame game) {
		this.game = game;
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		stage = new Stage();

		
		final TextButton buttonOldDemo = new TextButton("Old Demo", skin);
		buttonOldDemo.setWidth(200f);
		buttonOldDemo.setHeight(50f);
		buttonOldDemo.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 + 60f);
		
		buttonOldDemo.addListener(new ClickListener(){
			@Override 
            public void clicked(InputEvent event, float x, float y){
                startOldDemo();
            }
		});
		
		final TextButton buttonFallingContainers = new TextButton("Falling Containers", skin);
		buttonFallingContainers.setWidth(200f);
		buttonFallingContainers.setHeight(50f);
		buttonFallingContainers.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 - 10f);
		
		buttonFallingContainers.addListener(new ClickListener(){
			@Override 
            public void clicked(InputEvent event, float x, float y){
                startFallingContainers();
            }
		});

		stage.addActor(buttonOldDemo);
		stage.addActor(buttonFallingContainers);
		
		Gdx.input.setInputProcessor(stage);
	}
	
	private void startOldDemo(){
		game.setScreen(new OldDemo(game));
	}
	
	private void startFallingContainers(){
        game.setScreen(new FallingContainersGame(this.game));
	}
	
	@Override
	public void render(float delta) {       
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        stage.draw();
        batch.end();
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
		batch.dispose();
	}

}

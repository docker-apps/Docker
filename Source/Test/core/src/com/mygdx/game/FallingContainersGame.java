package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class FallingContainersGame implements Screen {

	private MyTestGame game;
	private Stage stage;
	private ExtendViewport viewport;

	public FallingContainersGame(MyTestGame game) {
		this.game = game;
		this.viewport = new ExtendViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		this.stage = new Stage(viewport);

//		ContainerActor testContainer = new ContainerActor(10,
//				stage.getHeight(), 40);
//		ContainerActor testContainer2 = new ContainerActor(400,
//				stage.getHeight(), 20);
//
//		this.stage.addActor(testContainer);
//		this.stage.addActor(testContainer2);
		Gdx.input.setInputProcessor(this.stage);
		Gdx.input.setCatchBackKey(true);

		Task task = new Task() {

			@Override
			public void run() {
				ContainerActor newContainer = new ContainerActor();
				int grid_with =(int) Math.floor(stage.getWidth() / newContainer.getWidth());
				Random r = new Random();
				newContainer.x = r.nextInt(grid_with) * (newContainer.getWidth()+1);
				newContainer.v =  r.nextInt(40) + 20;
				newContainer.y = stage.getHeight();
				
				stage.addActor(newContainer);
			}

			@Override
			public void cancel() {
			}
		};
		Timer.schedule(task, 0, 1);
	}
	
	public void exit(){
		game.setLastScreen();
	}

	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) ||
				Gdx.input.isKeyPressed(Input.Keys.BACK))
		{
			this.exit();
		}
		

		this.stage.act(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

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
		this.stage.dispose();
	}

}

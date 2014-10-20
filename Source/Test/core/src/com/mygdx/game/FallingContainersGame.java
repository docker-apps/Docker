package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class FallingContainersGame implements Screen {

	private Game game;
	private Stage stage;
	private ExtendViewport viewport;

	public FallingContainersGame(Game game) {
		this.viewport = new ExtendViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		this.stage = new Stage(viewport);

		ContainerActor testContainer = new ContainerActor(10,
				stage.getHeight(), 40);
		ContainerActor testContainer2 = new ContainerActor(400,
				stage.getHeight(), 20);

		this.stage.addActor(testContainer);
		this.stage.addActor(testContainer2);
		Gdx.input.setInputProcessor(this.stage);

		Task task = new Task() {

			@Override
			public void run() {
				Random r = new Random();
				int x = r.nextInt((int) stage.getWidth());
				int v = r.nextInt(40) + 20;
				stage.addActor(new ContainerActor(x, stage.getHeight(), v));
			}

			@Override
			public void cancel() {
			}
		};
		Timer.schedule(task, 2, 1);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.stage.act(Gdx.graphics.getDeltaTime());

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

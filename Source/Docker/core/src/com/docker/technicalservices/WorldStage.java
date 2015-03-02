package com.docker.technicalservices;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WorldStage extends Stage {
	Actor foreground = null;
	Actor background = null;
	
	public WorldStage(Viewport viewport) {
		super(viewport);
	}
	
	public WorldStage(Viewport viewport, Batch batch) {
		super(viewport, batch);
	}
	
	@Override
	public void draw () {
		Camera camera = getViewport().getCamera();
		camera.update();

		if (!getRoot().isVisible()) return;

		Batch batch = this.getBatch();
		if (batch != null) {
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			batch.setColor(1, 1, 1, 1f);
			if(this.background != null)
				background.draw(batch, 1);
			getRoot().draw(batch, 1);
			if(this.foreground != null)
				foreground.draw(batch, 1);
			batch.end();
		}

		//debug drawing doesn't work atm
		//if (super.debug) drawDebug();
	}

	@Override
	public void act (float delta) {
		if(this.background != null)
			this.background.act(delta);
		super.act(delta);
		if(this.foreground != null)
			this.foreground.act(delta);
	}

	public Actor getForeground() {
		return foreground;
	}


	public void setForeground(Actor foreground) {
		this.foreground = foreground;
	}


	public Actor getBackground() {
		return background;
	}


	public void setBackground(Actor background) {
		this.background = background;
	}
}

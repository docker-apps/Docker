package com.docker.technicalservices;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.docker.domain.world.Background;
import com.docker.domain.world.Foreground;

public class WorldStage extends Stage implements GestureListener {
	private static final int SHAKE_COOLDOWN = 10;
	private static final int SHAKE_FREQUENCY = 15;
	Actor foreground = null;
	Actor background = null;

	protected float shakeTimer;
	protected float shakeIntensityX;
	protected float shakeIntensityY;
	protected Vector3 cameraOriginalPosition;
	private float cameraOffsetX;
	private float cameraOffsetY;
	
	public WorldStage(Viewport viewport) {
		super(viewport);

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
        
		this.shakeTimer = 0;
		this.shakeIntensityX = shakeIntensityY = 0;
	}
	
	public WorldStage(Viewport viewport, Batch batch) {
		super(viewport, batch);
	}
	
	public void shakeScreen(float intensityX, float intensityY){
		this.shakeIntensityX = intensityX;
		this.shakeIntensityY = intensityY;
	}
	
	@Override
	public void draw () {
		Camera camera = getViewport().getCamera();
		cameraOriginalPosition = this.getCamera().position.cpy();
		camera.position.x = cameraOriginalPosition.x + cameraOffsetX;
		camera.position.y = cameraOriginalPosition.y + cameraOffsetY;
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
        /*
            batch.end();
            super.draw();
            batch.begin();
            */
			if(this.foreground != null)
				foreground.draw(batch, 1);
			batch.end();
		}

		//debug drawing doesn't work atm
//		//if (super.debug()) drawDebug();
		
		camera.position.x = cameraOriginalPosition.x;
		camera.position.y = cameraOriginalPosition.y;
		camera.update();
	}

	@Override
	public void act (float delta) {
		if(this.background != null)
			this.background.act(delta);
		super.act(delta);
		if(this.foreground != null)
			this.foreground.act(delta);
		
		//calculate current screenshake
		float cooldown = delta*SHAKE_COOLDOWN;
		if(Math.abs(shakeIntensityX) >= cooldown || Math.abs(shakeIntensityY) >= cooldown){
			shakeTimer+=delta;
			cameraOffsetX = (float) (Math.cos(shakeTimer*SHAKE_FREQUENCY)*shakeIntensityX);
			cameraOffsetY = (float) (Math.cos(shakeTimer*SHAKE_FREQUENCY)*shakeIntensityY);
			shakeIntensityX -= Math.signum(shakeIntensityX)*cooldown;
			shakeIntensityY -= Math.signum(shakeIntensityY)*cooldown;
		}
		else {
			shakeTimer = 0;
			cameraOffsetX = cameraOffsetY = 0;
		}
	}
	
	@Override
	public void dispose(){
		this.background = null;
		this.foreground = null;
		super.dispose();
	}

	public Actor getForeground() {
		return foreground;
	}


	public void setForeground(Actor foreground) {
		this.foreground = foreground;
	}
	
	public void setForeground(Foreground foreground){
		this.foreground = foreground;
		((Foreground) this.foreground).setStage(this);
	}


	public Actor getBackground() {
		return background;
	}

	public void setBackground(Actor background) {
		this.background = background;
	}
	
	public void setBackground(Background background) {
		this.background = background;
		((Background) this.background).setStage(this);
	}

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}

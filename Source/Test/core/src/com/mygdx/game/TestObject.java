package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class TestObject {
	private static final int FRAME_WIDTH = 13;
	private static final int FRAME_HEIGHT = 13;
	private static final int FRAME_COLS = 3;
	private static final int FRAME_ROWS = 1;
	private Texture texture;
	private Vector2 pos;
	private boolean toggleDir = false;
	private TextureRegion[] frames;
	private Animation animation;
	
	public TestObject(float x, float y){
		this(new Vector2(x, y));
	}
	
	public TestObject(Vector2 pos){
		this.setPos(pos);
		this.setTexture(new Texture("pacman.png"));

		this.frames = new TextureRegion[3];
		int frame_index = 0;
		for (int i = 0; i < FRAME_COLS; i++) {
			for (int j = 0; j < FRAME_ROWS; j++) {
				this.frames[frame_index++] = new TextureRegion(this.texture, i*FRAME_WIDTH, j*FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);
			}
		}
		this.setAnimation(new Animation(0.25f, this.frames));
	}
	
	public void step(){
		if(this.pos.x >= (Gdx.graphics.getWidth() - FRAME_WIDTH) || this.pos.x <= 0)
			toggleDir = !toggleDir;
		float mov = 100 * Gdx.graphics.getDeltaTime();
		if(toggleDir)
			mov *= -1;
		this.pos.x += mov;
	}
	
	public void setX(float x){
		this.pos.x = x;
	}
	
	public void setY(float y){
		this.pos.y = y;
	}
	
	public float getX(){
		return this.pos.x;
	}
	
	public float getY(){
		return this.pos.y;
	}
	
	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	public boolean isToggleDir() {
		return toggleDir;
	}

	public void setToggleDir(boolean toggleDir) {
		this.toggleDir = toggleDir;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}	
	
}
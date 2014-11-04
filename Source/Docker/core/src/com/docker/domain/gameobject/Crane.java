package com.docker.domain.gameobject;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Crane extends Actor {
	private int speed;
	private Container container;

	private TextureRegion body;
	private TextureRegion extensionLeft;
	private TextureRegion extensionLeftOuter;
	private TextureRegion extensionRight;
	private TextureRegion extensionRightOuter;

	/**
	 * @param speed the speed at which the crane can move.
	 * @param x the crane's initial position on the x-plane
	 * @param y the crane's initial position on the y-plane
	 */
	public Crane(int speed, float x, float y){
		super();
		this.setX(x);
		this.setY(y);
		this.speed = speed;
		this.container = null;

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));
		this.body = atlas.findRegion("crane_body");
		this.extensionLeft = atlas.findRegion("crane_extension_left");
		this.extensionLeftOuter = atlas.findRegion("crane_extension_left_outer");
		this.extensionRight = atlas.findRegion("crane_extension_right");
		this.extensionRightOuter = atlas.findRegion("crane_extension_right_outer");
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if(this.container != null){
			this.container.setX(this.getX());
			this.container.setY(this.getY());			
		}
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		this.container.draw(batch, parentAlpha);
		
		if(this.container != null){
			int length = this.container.getLength();
			float elementWidth = this.container.getElementWidth();
			float yPos = this.getY()+this.container.getHeight()-4;
			
			// Not great code, but it works. If you can create an elegant algorithm, feel free to improve.
			if(length == 5){
				batch.draw(this.extensionLeftOuter,
						this.getX(),
						yPos);
				batch.draw(this.extensionRightOuter,
						this.getX()+elementWidth*4,
						yPos);
				batch.draw(this.extensionLeft,
						this.getX()+elementWidth,
						yPos);
				batch.draw(this.extensionRight,
						this.getX()+elementWidth*3,
						yPos);
			}
			else if(length == 4){
				batch.draw(this.extensionLeftOuter,
						this.getX(),
						yPos);
				batch.draw(this.extensionRightOuter,
						this.getX()+elementWidth*3,
						yPos);
				batch.draw(this.extensionLeft,
						this.getX()+elementWidth*0.5f,
						yPos);
				batch.draw(this.extensionRight,
						this.getX()+elementWidth*2f,
						yPos);
			}
			else if(length == 3){
				batch.draw(this.extensionLeft,
						this.getX(),
						yPos);
				batch.draw(this.extensionRight,
						this.getX()+elementWidth*2f,
						yPos);
			}
			else if(length == 2){
				batch.draw(this.extensionLeft,
						this.getX(),
						yPos);
				batch.draw(this.extensionRight,
						this.getX()+elementWidth,
						yPos);
			}



			batch.draw(this.body, 
					this.getX()+((float)this.container.getWidth())/2-((float)this.body.getRegionWidth())/2, 
					this.getY()+this.container.getHeight()-4);
		}
	}

	/**
	 * @param container the container which the crane holds.
	 */
	public void setContainer(Container container){
		this.container = container;
	}
	
	/**
	 * @return wether the crane is holding a container.
	 */
	public boolean hasContainer(){
		return this.container != null;
	}

	/** 
	 * Removes the container which the crane is holding and returns it.
	 * 
	 * @return the container which the crane was holding, null if it's not holding one.
	 */
	public Container removeContainer(){
		Container container = this.container;
		this.container = null;
		return container;
	}
}

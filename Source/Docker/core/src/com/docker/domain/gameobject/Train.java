package com.docker.domain.gameobject;

import java.util.LinkedList;
import java.util.List;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Train extends Actor {
	private final float PADDING = 11;
	
	private int speed;
	private List<Container> containers;
	
	private TextureRegion platform_single;
	private TextureRegion platform_center;
	private TextureRegion platform_left;
	private TextureRegion platform_right;
	private TextureRegion wheel;
	
	/**
	 * @param speed Speed at which the train moves rightward
	 * @param x Initial Position on the x-plane
	 * @param y Position on the y-plane
	 */
	public Train(int speed, float x, float y) {
		super();
		this.setX(x);
		this.setY(y);
		this.speed = speed;
		this.containers = new LinkedList<Container>();
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));
		this.platform_single = atlas.findRegion("train_platform_single");
		this.platform_center = atlas.findRegion("train_platform_center");
		this.platform_left = atlas.findRegion("train_platform_left");
		this.platform_right = atlas.findRegion("train_platform_right");
		this.wheel = atlas.findRegion("train_wheel");
	}
	
	public void addContainer(Container container) {
		this.containers.add(container);
	}
	
	public Container popContainer() {
		throw new NotImplementedException();
	}
	
	@Override
	public void act(float delta) {
		this.setX(this.getX() + this.speed*delta);
		
		
		float xPos = this.getX();
		for (Container container : this.containers) {
			container.setX(xPos);
			container.setY(this.getY());
			xPos += container.getWidth() + this.PADDING;
		}
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) {
		for (Container container : this.containers) {
			batch.draw(
					this.wheel,
					container.getX(),
					container.getY() - 8);
			batch.draw(
					this.wheel,
					container.getX() + container.getWidth() - this.wheel.getRegionWidth(),
					container.getY() - 8);
			
			container.draw(batch, parentAlpha);
			
			if(container.getLength() == 1){
				batch.draw(
						this.platform_single,
						container.getX() - 6f,
						container.getY() - 4f);
			}
			else{
				float elementWidth = container.getElementWidth();
				batch.draw(
						this.platform_left,
						container.getX() - 6f,
						container.getY() - 4f);
				for (int i = 1; i <= container.getLength()-2; i++) {
					batch.draw(this.platform_center, 
							container.getX() + elementWidth*i, 
							container.getY() - 4f);			
				}
				batch.draw(this.platform_right, 
						container.getX() + elementWidth*(container.getLength()-1) - 3, 
						container.getY() - 4f);
			}
		}
	}
}

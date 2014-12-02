package com.docker.domain.gameobject;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Train extends Actor {
	private static final float PADDING = 11f;
	private static final float PLATFORM_OFFSET = 6f;

	private float speed;
	private Queue<Container> containers;

	private TextureRegion platform_single;
	private TextureRegion platform_center;
	private TextureRegion platform_left;
	private TextureRegion platform_right;
	private TextureRegion wheel;
	private float stateTime;

	private boolean indestructible = false;
	
	/**
	 * @param speed Speed at which the train moves rightward
	 * @param x Initial Position on the x-plane
	 * @param y Position on the y-plane
	 */	
	public Train(float speed, float x, float y) {
		super();
		this.setX(x);
		this.setY(y);
		this.speed = speed;
		this.containers = new LinkedList<Container>();
		this.stateTime = 0f;

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));
		this.platform_single = atlas.findRegion("train_platform_single");
		this.platform_center = atlas.findRegion("train_platform_center");
		this.platform_left = atlas.findRegion("train_platform_left");
		this.platform_right = atlas.findRegion("train_platform_right");
		this.wheel = atlas.findRegion("train_wheel");
	}
	
	public Train(Queue<Container> containerList, float speed){
		this(containerList, speed, 0, 0);
	}
	
	public Train(Queue<Container> containerList, float speed, float x, float y){
		this(speed, x, y);
		this.containers = containerList;
        float lastX = 0f;
        for (Container container : containerList) {
            lastX = lastX-container.getWidth() - PADDING;
            container.setX(lastX);
        }
    }

	public void addContainer(Container container){
        Container lastContainer = getLastContainer();
        container.setX(lastContainer.getX() - container.getWidth() - PADDING);
        this.containers.add(container);
    }

	public Container removeContainer(){
		return containers.remove();
	}
	public Container getFirstContainer(){
		return containers.peek();
	}

	@Override
	public void act(float delta){
		this.stateTime += delta;
		for (Container container : this.containers) {
            float xPos = container.getX();
            container.setY(this.getY());
            if (isIndestructible() || xPos + container.getWidth() < this.getStage().getWidth()) {
                xPos = speed*delta + container.getX();
                container.setX(xPos);
            }
        }
    }

    private Container getLastContainer() {
        Container lastContainer = getFirstContainer();
        for (Container container : containers) {
            float xPos = container.getX();
            if (xPos <= lastContainer.getX()) {
               lastContainer = container;
            }
        }
        return lastContainer;
    }

    public boolean hasContainers(){
		return this.containers.size() > 0;
	}

    @Override
	public void draw (Batch batch, float parentAlpha) {
		for (Container container : this.containers) {
			float bounceOffset = 0f;
			if(Math.round(this.stateTime*5)%2 == 0)
				bounceOffset = 1f;
			batch.draw(
					this.wheel,
					container.getX(),
					this.getY() - 8 + bounceOffset);
			batch.draw(
					this.wheel,
					container.getX() + container.getWidth() - this.wheel.getRegionWidth(),
					this.getY() - 8 + bounceOffset);

			container.draw(batch, parentAlpha);

			if(container.getLength() == 1){
				batch.draw(
						this.platform_single,
                        container.getX() - PLATFORM_OFFSET,
                        this.getY() - 4f + bounceOffset);
			} else {
				float elementWidth = container.getElementWidth();
				batch.draw(
						this.platform_left,
						container.getX() - PLATFORM_OFFSET,
						this.getY() - 4f + bounceOffset);
				for (int i = 1; i <= container.getLength()-2; i++) {
					batch.draw(this.platform_center,
							container.getX() + elementWidth*i,
							this.getY() - 4f + bounceOffset);
				}
				batch.draw(this.platform_right,
						container.getX() + elementWidth*(container.getLength()-1) - 3,
						this.getY() - 4f + bounceOffset);
			}
		}
	}
    
    /**
     * 
     * @return the size of the Container list
     */
    public int getContainerListSize(){
    	return containers.size();
    }

	/**
	 * @return  the speed at which the train moves rightward.
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed at which the train moves rightward.
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public boolean isIndestructible() {
		return indestructible;
	}

	public void setIndestructible(boolean indestructible) {
		this.indestructible = indestructible;
	}
}

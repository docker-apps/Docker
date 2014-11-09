package com.docker.domain.gameobject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Train extends Actor {
	private static final float PADDING = 11f;
	private static final float PLATFORM_OFFSET = 6f;

	private int speed;
	private Queue<Container> containers;

	private TextureRegion platform_single;
	private TextureRegion platform_center;
	private TextureRegion platform_left;
	private TextureRegion platform_right;
	private TextureRegion wheel;
	private float stateTime;
	
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
		this.stateTime = 0f;

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));
		this.platform_single = atlas.findRegion("train_platform_single");
		this.platform_center = atlas.findRegion("train_platform_center");
		this.platform_left = atlas.findRegion("train_platform_left");
		this.platform_right = atlas.findRegion("train_platform_right");
		this.wheel = atlas.findRegion("train_wheel");
	}

	public void addContainer(Container container){
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
        List<Container> toRemove = new ArrayList<Container>();
        float lastX = 0f;
		for (Container container : this.containers) {
            float xPos = container.getX();

            if (lastX == 0 || fits(container, lastX) || !toRemove.isEmpty()) {
                container.setY(this.getY());
                if (xPos - PLATFORM_OFFSET < this.getStage().getWidth()) {
                    xPos = speed*delta + container.getX();
                    container.setX(xPos);
                    lastX = xPos;
                } else {
                    toRemove.add(container);
                }
            } else {
                container.setX(container.getWidth()*-1);
            }
        }
        if (!toRemove.isEmpty()) containers.removeAll(toRemove);
        // leben abziehen
    }

    private boolean fits(Container container, float lastX) {
        return container.getX() + container.getWidth() + PADDING < lastX;
    }

    @Override
	public void draw (Batch batch, float parentAlpha) {
		for (Container container : this.containers) {
			float wheelOffset = 0f;
			if(Math.round(this.stateTime*5)%2 == 0)
				wheelOffset = 1f;
			batch.draw(
					this.wheel,
					container.getX(),
					container.getY() - 8 + wheelOffset);
			batch.draw(
					this.wheel,
					container.getX() + container.getWidth() - this.wheel.getRegionWidth(),
					container.getY() - 8 + wheelOffset);

			container.draw(batch, parentAlpha);

			if(container.getLength() == 1){
				batch.draw(
						this.platform_single,
						container.getX() - PLATFORM_OFFSET,
						container.getY() - 4f + wheelOffset);
			} else {
				float elementWidth = container.getElementWidth();
				batch.draw(
						this.platform_left,
						container.getX() - PLATFORM_OFFSET,
						container.getY() - 4f + wheelOffset);
				for (int i = 1; i <= container.getLength()-2; i++) {
					batch.draw(this.platform_center,
							container.getX() + elementWidth*i,
							container.getY() - 4f + wheelOffset);
				}
				batch.draw(this.platform_right,
						container.getX() + elementWidth*(container.getLength()-1) - 3,
						container.getY() - 4f + wheelOffset);
			}
		}
	}

	/**
	 * @return  the speed at which the train moves rightward.
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed at which the train moves rightward.
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}

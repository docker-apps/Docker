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
	private static final float PADDING = 11;

	private int speed;
	private Queue<Container> containers;

	private TextureRegion platform_single;
	private TextureRegion platform_center;
	private TextureRegion platform_left;
	private TextureRegion platform_right;
	private TextureRegion wheel;

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

	public void addContainer(Container container){
		this.containers.add(container);
	}

	public Container popContainer(){
		return containers.remove();
	}
	public Container getContainer(){
		return containers.peek();
	}

	@Override
	public void act(float delta){
        List<Container> toRemove = new ArrayList<Container>();
        float lastX = 0f;
		for (Container container : this.containers) {
            float xPos = container.getX();

            if (lastX == 0 || fits(container, lastX) || !toRemove.isEmpty()) {
                container.setY(this.getY());
                if (xPos + container.getWidth() < 600) {
                    xPos = speed + container.getX();
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
        return container.getWidth() + container.getX() + PADDING < lastX;
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
			} else {
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

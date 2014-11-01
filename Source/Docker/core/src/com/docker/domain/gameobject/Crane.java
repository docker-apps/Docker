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
	private TextureRegion extension_left;
	private TextureRegion extension_left_outer;
	private TextureRegion extension_right;
	private TextureRegion extension_right_outer;
	private List<TextureRegion> elements;

	public Crane(int speed, float x, float y){
		this.setX(x);
		this.setY(y);
		this.speed = speed;
		this.container = null;

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));
		this.body = atlas.findRegion("crane_body");
		this.extension_left = atlas.findRegion("crane_extension_left");
		this.extension_left_outer = atlas.findRegion("crane_extension_left_outer");
		this.extension_right = atlas.findRegion("crane_extension_right");
		this.extension_right_outer = atlas.findRegion("crane_extension_right_outer");

		this.elements = new ArrayList<TextureRegion>(5);
		this.elements.add(this.extension_left_outer);
		this.elements.add(this.extension_left);
		//this.elements.add(this.body);
		this.elements.add(this.extension_right);
		this.elements.add(this.extension_right_outer);
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
			
			if(length == 5){
				batch.draw(this.extension_left_outer,
						this.getX(),
						yPos);
				batch.draw(this.extension_right_outer,
						this.getX()+elementWidth*4,
						yPos);
				batch.draw(this.extension_left,
						this.getX()+elementWidth,
						yPos);
				batch.draw(this.extension_right,
						this.getX()+elementWidth*3,
						yPos);
			}
			else if(length == 4){
				batch.draw(this.extension_left_outer,
						this.getX(),
						yPos);
				batch.draw(this.extension_right_outer,
						this.getX()+elementWidth*3,
						yPos);
				batch.draw(this.extension_left,
						this.getX()+elementWidth*0.5f,
						yPos);
				batch.draw(this.extension_right,
						this.getX()+elementWidth*2f,
						yPos);
			}
			else if(length == 3){
				batch.draw(this.extension_left,
						this.getX(),
						yPos);
				batch.draw(this.extension_right,
						this.getX()+elementWidth*2f,
						yPos);
			}
			else if(length == 2){
				batch.draw(this.extension_left,
						this.getX(),
						yPos);
				batch.draw(this.extension_right,
						this.getX()+elementWidth,
						yPos);
			}



			batch.draw(this.body, 
					this.getX()+((float)this.container.getWidth())/2-((float)this.body.getRegionWidth())/2, 
					this.getY()+this.container.getHeight()-4);
		}
	}

	public void setContainer(Container container){
		this.container = container;
	}

	public Container removeContainer(){
		Container container = this.container;
		this.container = null;
		return container;
	}
}

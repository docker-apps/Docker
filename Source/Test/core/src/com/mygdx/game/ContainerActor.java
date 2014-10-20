package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ContainerActor extends Actor{
	
	private Texture texture = new Texture(Gdx.files.internal("2x1_container.png"));
	public float x;
	public float y;
	public float v;
	
	
	public ContainerActor(){
		this(0f, 0f, 20f);
	}
	
	public ContainerActor(float x, float y, float v){
		super();
		this.x = x;
		this.y = y;
		this.v = v;
		this.setBounds(this.x, this.y, texture.getWidth(), texture.getHeight());
	}
	
	@Override
	public void draw(Batch batch, float alpha){
		batch.draw(texture,x,y,this.getOriginX(),this.getOriginY(),this.getWidth(),
	            this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation(),0,0,
	            texture.getWidth(),texture.getHeight(),false,false);
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		
		boolean isColliding = false;
		for (Actor actor : this.getStage().getActors()) {
			if(actor instanceof ContainerActor){
				if(actor != this && isColliding(((ContainerActor)actor)) && this.y > actor.getY()){
					isColliding = true;
					break;
				}
			}
		}
		
		if(!isColliding && this.y > 0){
			this.y -= v*delta;
		}
		
		this.setBounds(this.x, this.y, texture.getWidth(), texture.getHeight());
	}
	
	private boolean isColliding(ContainerActor containerActor){
		return ((containerActor.getX() + containerActor.getWidth()) >= this.x &&
				containerActor.getX() <= (this.x + this.getWidth()) &&
				(containerActor.getY() + containerActor.getHeight()) >= this.y &&
				containerActor.getY() <= (this.y + this.getHeight()));
	}
}

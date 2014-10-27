package com.docker.domain.gameobject;

import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ship extends Actor {
	private int gridWidth;
	private int gridHeight;
	private int carryingCapacity;
	private List<Container> containers;
	
	private Texture body_left;
	private Texture body_center;
	private Texture body_right;
	private Texture tower;
	private Texture mast;
	
	public Ship(int gridWidth, int gridHeight, int carryingCapacity, float x, float y) {
		super();
		
		if(gridWidth <= 1)
			throw new IllegalArgumentException();	
		
		this.setX(x);
		this.setY(y);
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.carryingCapacity = carryingCapacity;
		this.containers = new ArrayList<Container>();
		
		this.body_left = new Texture(Gdx.files.internal("img/ship_body_left.png"));
		this.body_center = new Texture(Gdx.files.internal("img/ship_body_center.png"));
		this.body_right = new Texture(Gdx.files.internal("img/ship_body_right.png"));
		this.tower = new Texture(Gdx.files.internal("img/ship_tower.png"));
		this.mast = new Texture(Gdx.files.internal("img/ship_mast.png"));

		this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	public void addContainer(int gridX, int gridY, Container container){
		throw new NotImplementedException();
	}
	
	public boolean isFree(int gridX, int gridY){
		throw new NotImplementedException();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) {
		batch.draw(this.body_left, this.getX(), this.getY());
		batch.draw(this.mast, this.getX()+this.body_left.getWidth()-this.getElementWidth()-this.mast.getWidth()-1, this.getY()+this.body_left.getHeight());
		for (int i = 0; i < this.gridWidth-2; i++) {
			batch.draw(this.body_center, this.getX()+this.body_left.getWidth()+(getElementWidth()*i), this.getY());			
		}
		float bodyRightX = this.getX()+this.body_left.getWidth()+(getElementWidth()*(this.gridWidth-2));
		batch.draw(this.body_right, bodyRightX, this.getY());
		batch.draw(this.tower, bodyRightX+this.body_right.getWidth()-this.tower.getWidth()-1, this.getY()+this.body_right.getHeight());
	}
	
	@Override
	public float getWidth(){
		return this.body_left.getWidth() + this.body_center.getWidth()*this.gridWidth + this.body_right.getWidth();
	}
	
	public float getElementWidth(){
		return this.body_center.getWidth();
	}
}

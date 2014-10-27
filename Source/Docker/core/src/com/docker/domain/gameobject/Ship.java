package com.docker.domain.gameobject;

import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ship extends Actor {
	private int gridWidth;
	private int gridHeight;
	private int carryingCapacity;
	private List<Container> containers;
	
	private TextureRegion body_left;
	private TextureRegion body_center;
	private TextureRegion body_right;
	private TextureRegion tower;
	private TextureRegion mast;
	
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

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));
		this.body_left = atlas.findRegion("ship_body_left");
		this.body_center = atlas.findRegion("ship_body_center");
		this.body_right = atlas.findRegion("ship_body_right");
		this.tower = atlas.findRegion("ship_tower");
		this.mast = atlas.findRegion("ship_mast");

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
		batch.draw(this.mast, this.getX()+this.body_left.getRegionWidth()-this.getElementWidth()-this.mast.getRegionWidth()-1, this.getY()+this.body_left.getRegionHeight());
		for (int i = 0; i < this.gridWidth-2; i++) {
			batch.draw(this.body_center, this.getX()+this.body_left.getRegionWidth()+(getElementWidth()*i), this.getY());			
		}
		float bodyRightX = this.getX()+this.body_left.getRegionWidth()+(getElementWidth()*(this.gridWidth-2));
		batch.draw(this.body_right, bodyRightX, this.getY());
		batch.draw(this.tower, bodyRightX+this.body_right.getRegionWidth()-this.tower.getRegionWidth()-1, this.getY()+this.body_right.getRegionHeight());
	}
	
	@Override
	public float getWidth(){
		return this.body_left.getRegionWidth() + this.body_center.getRegionWidth()*this.gridWidth + this.body_right.getRegionWidth();
	}
	
	public float getElementWidth(){
		return this.body_center.getRegionWidth();
	}
}

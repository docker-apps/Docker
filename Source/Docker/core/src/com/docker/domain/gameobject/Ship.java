package com.docker.domain.gameobject;

import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
	private Container previewContainer;
	private Integer gridSize;
	private int[] topLine;
	private float[][] grid;
	private float yGridstart;
	private float xGridstart;
	
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
		
		this.gridSize = this.body_center.getRegionWidth();

		this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		
		this.xGridstart = this.getX()+body_left.getRegionWidth() - gridSize;
		this.yGridstart = this.getY()+body_center.getRegionHeight();
		createTopLineAndGrid();
	}
	
	//Die Position im Grid wird vom Schiff selberausgerechnet
	public boolean addContainer(float x, Container container){
		int xGrid = (int) Math.floor((double) (x-xGridstart)/gridSize); 
		float yGrid = posYIFit(xGrid, container.getLength());
		if(yGrid >= 0 &&  yGrid < gridHeight){
			container.setPosition((xGrid*gridSize)+xGridstart, (yGrid*gridSize)+yGridstart);
			this.containers.add(container);
			createTopLineAndGrid();
			return true;
		}else{
			return false;
		}
	}
	
	public void setPreviewContainer(float x, Container container){
		int xGrid = (int) Math.floor((double) (x-xGridstart)/gridSize); 
		float yGrid = posYIFit(xGrid, container.getLength());
		if(yGrid >= 0 ){
			previewContainer = new Container(container);
			previewContainer.setColor(Color.MAGENTA);
			if (yGrid < gridHeight) {
				previewContainer.setColor(Color.DARK_GRAY);
			}
			previewContainer.setPosition((xGrid*gridSize)+xGridstart, (yGrid*gridSize)+yGridstart);
		}else{
			previewContainer = null;
		}
	}
	
	public void showPossiblePosition(int X, Container container){
		
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
		if (previewContainer != null) {
			previewContainer.draw(batch, parentAlpha);
		}
		for (Container container : containers) {
			container.draw(batch, parentAlpha);
		}
	}
	
	@Override
	public float getWidth(){
		return this.body_left.getRegionWidth() + this.body_center.getRegionWidth()*(this.gridWidth-2) + this.body_right.getRegionWidth();
	}
	
	public float getElementWidth(){
		return this.body_center.getRegionWidth();
	}
	
	/**
	 * Returns on with GridY the Container fits, 
	 * if result is negativ, it's not possible to Fit it in this GridX
	 * @param gridX
	 * @param size
	 * @return 
	 */
	public float posYIFit(int gridX, int size){
		float NoSpace = this.gridWidth - (gridX + size);
		if (NoSpace < 0 || gridX < 0) {
			return -1;
		}
		if(size == 1){
			return (topLine[gridX]);
		}
		float topline = posYIFit(gridX + 1, size -1 );
		if (topLine[gridX] > topline) {
			return (topLine[gridX]);
		}
		return topline;
	}
	
	public void createTopLineAndGrid(){
		this.topLine = new int[gridWidth];
		this.grid = new float[gridWidth][gridHeight];
		for (Container container : containers) {
			int gridX = (int) (container.getX()-xGridstart)/gridSize;
			int gridY = (int) ((container.getY()-yGridstart)/gridSize);
			int lenght = container.getLength();
			int weightPerLenght = lenght/container.getWeight();
			while (lenght > 0) {
				grid[gridX][gridY] = weightPerLenght;
				if (topLine[gridX] < gridY+1) {
					topLine[gridX] = gridY+1;
				}
				gridX++;
				lenght--;
			}
		}
	} 
	
	public float[][] getGrid(){
		return this.grid;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public int getCarryingCapacity() {
		return carryingCapacity;
	}

	public void setCarryingCapacity(int carryingCapacity) {
		this.carryingCapacity = carryingCapacity;
	}

	public List<Container> getContainers() {
		return containers;
	}

	public void setContainers(List<Container> containers) {
		this.containers = containers;
	}
	
}

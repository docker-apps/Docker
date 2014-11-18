package com.docker.domain.gameobject;

import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class Ship extends Actor {
	private int gridWidth;
	private int gridHeight;
	private int breakThreshold;
	private int capsizeThreshold;
	private List<Container> containers;
	private Container previewContainer;
	private Integer gridSize;
	private int[] topLine;
	private float[][] grid;
	private float yGridstart;
	private float xGridstart;

    private Sound containerSound;
	
	private TextureRegion body_left;
	private TextureRegion body_center;
	private TextureRegion body_right;
	private TextureRegion tower;
	private TextureRegion mast;
	
	public Ship(int gridWidth, int gridHeight, int capsizeThreshold, int breakThreshold, float x, float y) {
		super();
		
		if(gridWidth <= 1)
			throw new IllegalArgumentException();	
		
		this.setX(x);
		this.setY(y);
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.capsizeThreshold = capsizeThreshold;
		this.breakThreshold = breakThreshold;
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
		
		containerSound = Gdx.audio.newSound(Gdx.files.internal("container_load.wav"));
		createTopLineAndGrid();
	}
	
	/**
	 * Adds the Container if possible at the given X position
	 * if returned false, it was not possible to set the Container
	 * 
	 * @param x
	 * @param container
	 * @return
	 */
	public boolean addContainer(float x, Container container){
		Vector2 gridCoords = getGridCoords(x, container.getLength());
		if(gridCoords.y >= 0 &&  gridCoords.y < gridHeight){
			Vector2 realCoords = getRealCoord(gridCoords);
			container.setPosition(realCoords.x, realCoords.y);
			this.containers.add(container);
			containerSound.play();
			createTopLineAndGrid();
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Sets a Preview Container, where the Container possible could be set.
	 * 
	 * @param x
	 * @param container
	 */
	public void setPreviewContainer(float x, Container container){
		Vector2 gridCoords = getGridCoords(x, container.getLength());
		if(gridCoords.y >= 0 ){
			previewContainer = new Container(container);
			previewContainer.setColor(new Color(1f, 0f, 0f, 0.5f));
			if (gridCoords.y < gridHeight) {
				previewContainer.setColor(new Color(1f, 1f, 1f, 0.5f));
			}
			Vector2 realCoords = getRealCoord(gridCoords);
			previewContainer.setPosition(realCoords.x, realCoords.y);
		}else{
			previewContainer = null;
		}
	}
	
	/**
	 * 
	 * @param x
	 * @param containerLenght
	 * @return
	 */
	private int getXGrid(float x, int containerLenght) {
		int xGrid = (int) Math.floor((double) (x-xGridstart)/gridSize); 
        if(xGrid < 0){
        	xGrid = 0;
        }
        float noSpace = this.gridWidth- (xGrid + containerLenght);
        if (noSpace < 0 ) {
			xGrid = this.gridWidth-containerLenght;
		}
		return xGrid;
	}
	

	/**
	 * 
	 * @param x
	 * @param containerLenght
	 * @return
	 */
	private Vector2 getGridCoords(float x, int containerLenght){
		Vector2 gridCoords = new Vector2();
		gridCoords.x = getXGrid(x, containerLenght);
		gridCoords.y = getYGrid((int) gridCoords.x, containerLenght);
		return gridCoords; 
	}
	
	
	/**
	 * Returns Coordinates where the Container will be places
	 * If it returns null the Container can't be placed at this place.
	 * 
	 * @param x
	 * @param container
	 * @return 
	 */
	public Vector2 getRealCoord(float x, Container container){
		Vector2 gridCoords = getGridCoords(x, container.getLength());
		Vector2 realCoords = null;
		if (gridCoords.y < gridHeight) {
			realCoords = getRealCoord(gridCoords);
		}
		return realCoords;
	}
	

	/**
	 * 
	 * @param gridCoords
	 * @return
	 */
	private Vector2 getRealCoord(Vector2 gridCoords){
		Vector2 realCoords = new Vector2();
		realCoords.x = gridCoords.x * gridSize + xGridstart;
		realCoords.y = gridCoords.y * gridSize + yGridstart;
		return realCoords;
	}


	/**
	 * Method which will be called to make the ship sail away.
	 * 
	 * @param container
	 * @param ship
	 * @param x
	 * @param y
	 */
	public void takeOff(Container container, final Ship ship, final float x, float y){
		// add the container to the crane
		
//		this.setContainer(container);
		
		// calculate the animation duration from the distance to the target and the cranes speed
//		float xDistance = Math.abs(this.getX() - x);
//		float yDistance = Math.abs(this.getY() - y);
//		float distance = (float) Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
//		float duration = distance / this.speed;
		
		// create a new Move-To Action
//		MoveToAction moveAction = new MoveToAction();
//		moveAction.setPosition(x, y);
//		moveAction.setDuration(duration);
		
		// create an action which deploys the container to the ship
//		Action completeAction = new Action(){
//		    public boolean act( float delta ) {
//		        // give the container to the ship here, preferrably to a grid coordinate
//		    	ship.addContainer(x, removeContainer());
//		    	return true;
//		    }
//		};
		
		// chain the two actions and add it to this actor
//		SequenceAction actions = new SequenceAction(moveAction, completeAction);
//		this.addAction(actions);
	}
	

	/**
	 * Can be called to check if the ship is taking off.
	 * 
	 * @return
	 */
	public boolean isTakingOff(){
		return this.getActions().size > 0;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		this.xGridstart = this.getX()+body_left.getRegionWidth() - gridSize;
		this.yGridstart = this.getY()+body_center.getRegionHeight();
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
		return 
				this.body_left.getRegionWidth() + 
				this.body_center.getRegionWidth()*(this.gridWidth-2) + 
				this.body_right.getRegionWidth();
	}
	
	public float getElementWidth(){
		return this.body_center.getRegionWidth();
	}
	
	/**
	 * Returns on with GridY the Container fits, 
	 * if result is negativ, it's not possible to Fit it in this GridX
	 * @param gridX
	 * @param lenght
	 * @return 
	 */
	public int getYGrid(int gridX, int lenght){
		if(lenght == 1){
			return (topLine[gridX]);
		}
		int topline = getYGrid(gridX + 1, lenght -1 );
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
			float weightPerLenght = (float)container.getWeight()/(float)lenght;
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

	public int getBreakThreshold() {
		return breakThreshold;
	}

	public void setBreakThreshold(int breakThreshold) {
		this.breakThreshold = breakThreshold;
	}

	public int getCapsizeThreshold() {
		return capsizeThreshold;
	}

	public void setCapsizeThreshold(int capsizeThreshold) {
		this.capsizeThreshold = capsizeThreshold;
	}

	public List<Container> getContainers() {
		return containers;
	}

	public void setContainers(List<Container> containers) {
		this.containers = containers;
	}
	
}

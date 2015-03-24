package com.docker.domain.gameobject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.docker.technicalservices.Resource;

/**
 * @author HAL9000
 *
 * The Foreground is the foremost graphical plane displayed during the game.
 */
public class Foreground extends Actor {
	protected static final Color WATER_BORDER_COLOR = Color.valueOf("126392");
	protected static final Color WATER_COLOR = Color.valueOf("2c98d6");
	protected static final float DEFAULT_WATERLEVEL = 20;
	protected static final float LIFE_PADDING = 5;

	private Stage stage;
	private float waterLevel;
	private float capsizeValue;
	private float bubbleXOffset;
	private int remainingLives;
	private int remainingShips;
	
	private TextureRegion dock;
	private TextureRegion waterLevelBase;
	private TextureRegion waterLevelMarkings;
	private TextureRegion waterLevelBubble;
	private TextureRegion liveContainer;
	private TextureRegion liveSaver;
	private Array<AtlasRegion> waterMovement;
	private Array<AtlasRegion> shipReflection;
	protected Animation waterMovementAnimation;
	private Animation shipReflectionAnimation;
	private List<Vector2> waterMovementPositions;
	private ShapeRenderer shapeRenderer;
	protected float stateTime;
	
	/**
	 * @param width The width over which the foreground spans. Usually equals the stages/screens width.
	 * @param waterLevel The height of the water plane.
	 */
	public Foreground(Stage stage, float waterLevel) {
		super();
		this.stage = stage;
		this.setX(0);
		this.setY(0);
		this.setWaterLevel(waterLevel);
		this.shapeRenderer = new ShapeRenderer();
		this.stateTime = 0f;
		this.bubbleXOffset = 0f;
		this.remainingLives = 0;
		this.remainingShips = 0;
		
		this.dock = Resource.findRegion("fg_dock");
		this.waterLevelBase = Resource.findRegion("water_level_base");
		this.waterLevelMarkings = Resource.findRegion("water_level_markings");
		this.waterLevelBubble = Resource.findRegion("water_level_bubble");
		this.waterMovement = Resource.findRegions("fg_water_movement");
		this.shipReflection = Resource.findRegions("fg_ship_reflection");
		this.liveContainer = Resource.findRegion("live_container");
		this.liveSaver = Resource.findRegion("live_saver");
		
		this.waterMovementAnimation = new Animation(0.2f, this.waterMovement);
		this.waterMovementAnimation.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		this.shipReflectionAnimation = new Animation(0.2f, this.shipReflection);
		this.shipReflectionAnimation.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		this.waterMovementPositions = new ArrayList<Vector2>();
		Random rand = new Random();
		for (int i = 0; i < 40; i++) {
			Vector2 position = new Vector2(
					rand.nextFloat()*this.getWidth(), 
					rand.nextFloat()*(this.getWaterLevel()-2));
			this.waterMovementPositions.add(position);
		}		
	}
	
	/**
	 * Get an Foreground instance with the default water level.
	 * 
	 * @param width The width over which the foreground spans. Usually equals the stages/screens width.
	 */
	public Foreground(Stage stage){
		this(stage, DEFAULT_WATERLEVEL);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		float goalXPos = 13*this.capsizeValue;
		if(Math.abs(this.bubbleXOffset - goalXPos) > 0.01)
			this.bubbleXOffset += (goalXPos- this.bubbleXOffset+1)*delta;
		else
			this.bubbleXOffset = goalXPos;
		if(this.bubbleXOffset >= 0)
			this.bubbleXOffset = Math.min(15, this.bubbleXOffset);
		else
			this.bubbleXOffset = Math.max(-15, this.bubbleXOffset);
			
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		this.stateTime += Gdx.graphics.getDeltaTime();

		drawDock(batch);
		drawWaterLevel(batch);
		drawWaterPlane(batch);
		drawWaterAnimation(batch);
		drawHud(batch);
	}
	
	public void drawDock(Batch batch, Color tint){
		batch.setColor(tint);
		drawDock(batch);
		batch.setColor(Color.WHITE);
	}
	
	public void drawDock(Batch batch){
		batch.draw(
				this.dock,
				this.getWidth() - this.dock.getRegionWidth(),
				this.getWaterLevel() - 3);
	}
	
	public void drawWaterLevel(Batch batch, Color tint){
		batch.setColor(tint);
		drawWaterLevel(batch);
		batch.setColor(Color.WHITE);
	}
	
	public void drawWaterLevel(Batch batch){
		batch.draw(this.waterLevelBase, this.getWidth()-39, 30);
		if(Math.abs(this.bubbleXOffset) >= 13)
			batch.setColor(Color.RED);
		batch.draw(this.waterLevelBubble, this.getWidth()-this.bubbleXOffset-23, 32);
		batch.setColor(Color.WHITE);
		batch.draw(this.waterLevelMarkings, this.getWidth()-39, 30);
	}
	
	public void drawWaterPlane(Batch batch, Color tint){
		batch.setColor(tint);
		drawWaterPlane(batch);
		batch.setColor(Color.WHITE);
	}
	
	public void drawWaterPlane(Batch batch){
		batch.end();
		shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(WATER_COLOR);
		shapeRenderer.rect(
				this.getX(), 
				this.getY(), 
				this.getWidth(), 
				this.getWaterLevel()-1);
		shapeRenderer.setColor(WATER_BORDER_COLOR);
		shapeRenderer.rect(
				this.getX(), 
				this.getY()+this.getWaterLevel()-1,
				this.getX()+this.getWidth(), 
				1);
		shapeRenderer.end();
		batch.begin();
	}
	
	public void drawWaterAnimation(Batch batch, Color tint){
		batch.setColor(tint);
		drawWaterAnimation(batch);
		batch.setColor(Color.WHITE);
	}
	
	public void drawWaterAnimation(Batch batch){
		TextureRegion shipReflectionFrame = this.shipReflectionAnimation.getKeyFrame(stateTime, true);
		batch.draw(
				shipReflectionFrame,
				this.getX()+((this.getWidth()-shipReflectionFrame.getRegionWidth()) / 2),
				this.getY()+this.getWaterLevel() - shipReflectionFrame.getRegionHeight());
		
		for (Vector2 position : this.waterMovementPositions) {
			batch.draw(
					this.waterMovementAnimation.getKeyFrame(stateTime, true),
					position.x,
					position.y);
			if(position.y > 0){
				position.add(0, -0.05f);
			}
			else{
				Random rand = new Random();
				position.x = rand.nextFloat()*this.getWidth();
				position.y = (this.getWaterLevel()-2);
			}
		}
	}
	
	public void drawHud(Batch batch){
		for (int i = 0; i < this.remainingLives; i++) {
			float xOffset = (liveContainer.getRegionWidth()+LIFE_PADDING)*(i+1);
			batch.draw(this.liveContainer, this.getWidth()-xOffset, this.getHeight()-51);
		}
		float xOffset = LIFE_PADDING;
		for (int i = 0; i < this.remainingShips; i++) {
			batch.draw(this.liveSaver, xOffset, this.getHeight()-51);
			xOffset += (liveSaver.getRegionWidth()+LIFE_PADDING);
		}
	}

	/**
	 * @return the height of the water plane
	 */
	public float getWaterLevel() {
		return waterLevel;
	}

	/**'	
	 * @param waterLevel the height of the water plane
	 */
	public void setWaterLevel(float waterLevel) {
		this.waterLevel = waterLevel;
	}
	
	/**
	 * The CapsizeValue is displayed by the water level.
	 * 
	 * @param capsizeValue the current capsizeValue from the loadRating.
	 */
	public void setCapsizeValue(float capsizeValue){
		this.capsizeValue = capsizeValue;
	}

	/**
	 * @return the amount of remaining lives to display.
	 */
	public int getRemainingLives() {
		return remainingLives;
	}

	/**
	 * @param lives the amount of remaining lives to display.
	 */
	public void setRemainingLives(int lives) {
		this.remainingLives = lives;
	}

	/**
	 * @return the amount of remaining ships to display.
	 */
	public int getRemainingShips() {
		return remainingShips;
	}

	/**
	 * @param ships the amount of remaining ships to display.
	 */
	public void setRemainingShips(int ships) {
		this.remainingShips = ships;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public float getWidth(){
		return this.stage.getWidth();
	}
	
	public float getHeight(){
		return this.stage.getHeight();
	}
}

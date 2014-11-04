package com.docker.domain.gameobject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class Foreground extends Actor {
	
	private float waterLevel;
	private TextureRegion dock;
	private Array<AtlasRegion> waterMovement;
	private Array<AtlasRegion> shipReflection;
	private Animation waterMovementAnimation;
	private Animation shipReflectionAnimation;
	private List<Vector2> waterMovementPositions;
	private ShapeRenderer shapeRenderer;
	float stateTime;
	
	/**
	 * @param width The width over which the foreground spans. Usually equals the stages/screens width.
	 * @param waterLevel The height of the water plane.
	 */
	public Foreground(float width, float waterLevel) {
		super();
		this.setX(0);
		this.setY(0);
		this.setWidth(width);
		this.setWaterLevel(waterLevel);
		this.shapeRenderer = new ShapeRenderer();
		this.stateTime = 0f;
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));
		this.dock = atlas.findRegion("fg_dock");
		this.waterMovement = atlas.findRegions("fg_water_movement");
		this.shipReflection = atlas.findRegions("fg_ship_reflection");
		
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
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		this.stateTime += Gdx.graphics.getDeltaTime();
		
		batch.draw(
				this.dock,
				this.getWidth() - this.dock.getRegionWidth(),
				this.getWaterLevel() - 3);
		
		//draw water plane
		batch.end();
		shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.valueOf("2c98d6"));
		shapeRenderer.rect(
				this.getX(), 
				this.getY(), 
				this.getWidth(), 
				this.getWaterLevel()-1);
		shapeRenderer.setColor(Color.valueOf("126392"));
		shapeRenderer.rect(
				this.getX(), 
				this.getY()+this.getWaterLevel()-1,
				this.getX()+this.getWidth(), 
				1);
		shapeRenderer.end();
		
		batch.begin();
		
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
//			if(this.waterMovementAnimation.isAnimationFinished(stateTime)){
//				Random rand = new Random();
//				position.set(rand.nextFloat()*this.getWidth(), rand.nextFloat()*this.getWaterLevel());
//			}
		}		
	}

	/**
	 * @return the height of the water plane
	 */
	public float getWaterLevel() {
		return waterLevel;
	}

	/**
	 * @param waterLevel the height of the water plane
	 */
	public void setWaterLevel(float waterLevel) {
		this.waterLevel = waterLevel;
	}	
}

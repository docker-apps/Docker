package com.docker.ui.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.docker.domain.game.Level;
import com.docker.domain.gameobject.Train;
import com.docker.technicalservices.Resource;

/**
 * @author HAL9000
 *
 * The Background to display in the Menus.
 */
public class MenuBackground extends Actor {
	private float stateTime;
	
	private static final float SKY_HEIGHT = 50f;
	private static final int WATER_MOVEMENT_AMOUNT = 1000;
	private static final float TRAIN_SPEED = 10f;
	private static final float AQUEDUCT_SPEED = -20f;
	private static final float MINI_SHIP_SPEED = -5f;
	private static final float CLOUD_SPEED = 0.1f;
	
	private ShapeRenderer shapeRenderer;

	private TextureRegion aqueductElement;
	private TextureRegion harbor;
	private TextureRegion skyline;
	private float aqueductOffset;
	private Array<AtlasRegion> miniShip;
	private float miniShipXPos;
	private Array<AtlasRegion> bird;
	private Array<AtlasRegion> cloud;
	private int cloudIndex;
	private Vector2 cloudPosition;
	private Animation miniShipAnimation;
	private Animation birdAnimation;
	private Array<AtlasRegion> waterMovement;
	private Animation waterMovementAnimation;
	private List<Vector2> waterMovementPositions;
	
	private Train train;

	/**
	 * @param width the width of the Background. Usually equals the width of the screen.
	 * @param height the height of the Background. Usually equals the height of the screen.
	 */
	public MenuBackground(float width, float height){
		this.setWidth(width);
		this.setHeight(height);
		this.shapeRenderer = new ShapeRenderer();

		Random rand = new Random();
		TextureAtlas atlas =  Resource.getDockerTextureAtlas();
		this.aqueductElement = atlas.findRegion("aqueduct");
		this.harbor = atlas.findRegion("harbor");
		this.skyline = atlas.findRegion("skyline");
		
		this.miniShip = atlas.findRegions("mini_ship");
		this.miniShipAnimation = new Animation(0.3f, this.miniShip);
		this.miniShipAnimation.setPlayMode(PlayMode.LOOP);
		this.miniShipXPos = this.getWidth()-20f;
		
		this.bird = atlas.findRegions("bg_bird");
		this.birdAnimation = new Animation(0.2f, this.bird);

		this.cloud = atlas.findRegions("bg_cloud");
		this.cloudIndex = rand.nextInt(2);
		this.cloudPosition = new Vector2(rand.nextFloat()*this.getWidth(), getHorizonHeight() + rand.nextFloat()*SKY_HEIGHT);
		
		this.waterMovement = atlas.findRegions("fg_water_movement");
		this.waterMovementAnimation = new Animation(0.2f, this.waterMovement);
		this.waterMovementAnimation.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		this.waterMovementPositions = new ArrayList<Vector2>();
		for (int i = 0; i < WATER_MOVEMENT_AMOUNT; i++) {
			Vector2 position = new Vector2(
					rand.nextFloat()*(this.getWidth()+20)-10, 
					rand.nextFloat()*(getHorizonHeight() - 2));
			this.waterMovementPositions.add(position);
		}
		
		this.train = Level.loadLevel().getTrain();
		this.train.setPosition(20f, this.aqueductElement.getRegionHeight()+8);
		this.train.setIndestructible(true);
		this.train.setSpeed(TRAIN_SPEED);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		//move cloud
		if(this.cloudPosition.x > this.getX() + this.getWidth()){
			Random rand = new Random();
			this.cloudIndex = rand.nextInt(2);
			this.cloudPosition = new Vector2(
					this.getX() - this.cloud.get(this.cloudIndex).getRegionWidth(), 
					rand.nextFloat()*this.getHeight());
		}
		else{
			this.cloudPosition.add(CLOUD_SPEED, 0);
		}
		
		this.train.act(delta);
		
		if(Math.abs(this.aqueductOffset) < this.aqueductElement.getRegionWidth())
			this.aqueductOffset += AQUEDUCT_SPEED * delta;
		else
			this.aqueductOffset = AQUEDUCT_SPEED * delta;
		
		if(this.miniShipXPos + this.miniShip.get(0).getRegionWidth() + 60 > 0)
			this.miniShipXPos += MINI_SHIP_SPEED * delta;
		else
			this.miniShipXPos = this.getWidth() + this.miniShip.get(0).getRegionWidth();
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) {
		this.stateTime += Gdx.graphics.getDeltaTime();
		float dayTime = (float) Math.PI/2;
		float dayTimeSin = (float) Math.sin(dayTime);

		//draw sky
		batch.end();
		shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0.5f+dayTimeSin*0.29f, 0.2f+dayTimeSin*0.69f,dayTimeSin*0.95f,1f);
		shapeRenderer.rect(0f, 0f, this.getWidth(), this.getHeight());
		shapeRenderer.end();
		batch.begin();

		// draw sun
		Pixmap pixmap = new Pixmap(21, 21, Format.RGBA8888);
		pixmap.setColor(1f, 0.5f+dayTimeSin*0.5f, dayTimeSin*0.8f, 1f);
		pixmap.fillCircle(10, 10, 10);
		
		batch.draw(new Texture(pixmap),
				this.getWidth()/2+(float)Math.cos(dayTime)*(this.getWidth()/2-20),
				this.getHorizonHeight()-21+dayTimeSin*50);
		
		
		batch.draw(
				this.cloud.get(this.cloudIndex),
				this.cloudPosition.x,
				this.cloudPosition.y);

		batch.draw(
				this.birdAnimation.getKeyFrame(stateTime, true),
				40f,
				this.getHeight()-20);

		batch.draw(
				this.birdAnimation.getKeyFrame(stateTime+1, true),
				60f,
				this.getHeight()-30);

		batch.draw(
				this.birdAnimation.getKeyFrame(stateTime+1, true),
				this.getWidth()-40,
				this.getHeight()-10);
		
		//draw water
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.valueOf("2c98d6"));
		shapeRenderer.rect(
				this.getX(), 
				this.getY(), 
				this.getWidth(), 
				this.getHeight()-50);
		shapeRenderer.setColor(Color.valueOf("126392"));
		shapeRenderer.rect(
				this.getX(), 
				this.getY()+getHorizonHeight(),
				this.getX()+this.getWidth(), 
				1);
		shapeRenderer.end();
		batch.begin();
		
		// draw water animation
		for (Vector2 position : this.waterMovementPositions) {
			TextureRegion frame = this.waterMovementAnimation.getKeyFrame((float) (stateTime+position.y), true);
			batch.draw(
					frame,
					position.x,
					position.y, 
					frame.getRegionWidth() / Math.max(1f, (float) Math.exp(position.y/100)),
					frame.getRegionHeight());
		}
		
		batch.draw(this.skyline, this.getWidth()-this.skyline.getRegionWidth()-10f, this.getHorizonHeight() + 1);
		
		batch.draw(harbor, this.getWidth() - this.harbor.getRegionWidth(), 70f);
		
		batch.draw(miniShipAnimation.getKeyFrame(stateTime), this.miniShipXPos, 57f);
		
		this.train.draw(batch, parentAlpha);

		// draw aqueduct
		float aqueductWidth = this.aqueductElement.getRegionWidth();
		for (int i = 0; i < this.getWidth()/aqueductWidth+1; i++) {
			batch.draw(this.aqueductElement, aqueductOffset + aqueductWidth*i, 0f);
		}
	}
	
	private float getHorizonHeight(){
		return this.getHeight() - SKY_HEIGHT;
	}
}

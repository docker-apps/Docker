package com.docker.domain.gameobject;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.docker.technicalservices.Resource;

public class Background extends Actor {

	private static final Color HARBOR_COLOR = Color.valueOf("acbfca");
	private static final Color SKY_COLOR = Color.valueOf("cae4f3");
	private float harborLevel = 30f;
	private float cloudSpeed = 0.1f;
	private Array<AtlasRegion> bird;
	private Array<AtlasRegion> cloud;
	private TextureRegion crane;
	private TextureRegion tunnel;
	private int cloudIndex;
	private Vector2 cloudPosition;
	private Animation birdAnimation;
	private ShapeRenderer shapeRenderer;
	private float stateTime;


	public Background(float width, float height){
		super();
		this.setX(0);
		this.setY(0);
		this.setWidth(width);
		this.setHeight(height);
		this.shapeRenderer = new ShapeRenderer();
		this.stateTime = 0f;

		this.bird = Resource.findRegions("bg_bird");
		this.cloud = Resource.findRegions("bg_cloud");
		this.crane = Resource.findRegion("bg_crane");
		this.tunnel = Resource.findRegion("bg_train_tunnel");

		this.birdAnimation = new Animation(0.2f, this.bird);

		Random rand = new Random();
		this.cloudIndex = rand.nextInt(2);
		this.cloudPosition = new Vector2(rand.nextFloat()*this.getWidth(), rand.nextFloat()*this.getHeight());

	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if(this.cloudPosition.x > this.getX() + this.getWidth()){
			Random rand = new Random();
			this.cloudIndex = rand.nextInt(2);
			this.cloudPosition = new Vector2(
					this.getX() - this.cloud.get(this.cloudIndex).getRegionWidth(), 
					rand.nextFloat()*this.getHeight());
		}
		else{
			this.cloudPosition.add(this.cloudSpeed, 0);
		}
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		this.stateTime += Gdx.graphics.getDeltaTime();

		drawSky(batch);
		drawCloud(batch);
		drawBirds(batch);
		drawHarborPlatform(batch);
		drawCranes(batch);		
	}
	
	public void drawSky(Batch batch){
		drawSky(batch, SKY_COLOR);
	}
	
	public void drawSky(Batch batch, Color color){
		batch.end();
		shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color);
		shapeRenderer.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		shapeRenderer.end();
		batch.begin();
	}
	
	public void drawCloud(Batch batch, Color tint){
		batch.setColor(tint);
		drawCloud(batch);
		batch.setColor(Color.WHITE);
	}
	
	public void drawCloud(Batch batch){
		batch.draw(
				this.cloud.get(this.cloudIndex),
				this.cloudPosition.x,
				this.cloudPosition.y);
	}
	
	public void drawBirds(Batch batch, Color tint){
		batch.setColor(tint);
		drawBirds(batch);
		batch.setColor(Color.WHITE);
	}
	
	public void drawBirds(Batch batch){
		batch.draw(
				this.birdAnimation.getKeyFrame(stateTime, true),
				this.getWidth() / 4,
				this.getHeight() / 2);

		batch.draw(
				this.birdAnimation.getKeyFrame(stateTime, true),
				this.getWidth() / 4.5f,
				this.getHeight() / 1.5f);
	}
	
	public void drawHarborPlatform(Batch batch){
		drawHarborPlatform(batch, HARBOR_COLOR);
	}
	
	public void drawHarborPlatform(Batch batch, Color color){
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color);
		shapeRenderer.rect(this.getX(), this.getY(), this.getWidth(), this.harborLevel);
		shapeRenderer.end();
		batch.begin();
	}
	
	public void drawCranes(Batch batch, Color tint){
		batch.setColor(tint);
		drawCloud(batch);
		batch.setColor(Color.WHITE);
	}

	public void drawCranes(Batch batch){
		for(int i=0;i < 3;i++){
			batch.draw(
					this.crane,
					5+(this.crane.getRegionWidth()+20)*i,
					this.getX()+this.harborLevel);
		}
		
		for(float i=0;i<this.getWidth()+this.tunnel.getRegionWidth();i+=this.tunnel.getRegionWidth()){
			batch.draw(
					this.tunnel,
					i,
					this.getHeight()-this.tunnel.getRegionHeight());
		}
		batch.setColor(Color.WHITE);
	}
}

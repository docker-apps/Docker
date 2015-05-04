package com.docker.domain.world;

import java.util.ArrayList;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.docker.technicalservices.Resource;

public class Background extends Actor {

	protected static final Color HARBOR_COLOR = Color.valueOf("acbfca");
	protected static final Color SKY_COLOR = Color.valueOf("cae4f3");
	
	protected Stage stage;
	
	protected float harborLevel = 30f;
	protected Array<AtlasRegion> bird;
	protected ArrayList<Cloud> clouds;
	protected TextureRegion crane;
	protected TextureRegion tunnel;
	protected Animation birdAnimation;
	protected ShapeRenderer shapeRenderer;
	protected float stateTime;


	public Background(Stage stage){
		super();
		this.stage = stage;
		this.setX(0);
		this.setY(0);
		this.shapeRenderer = new ShapeRenderer();
		this.stateTime = 0f;

		this.bird = Resource.findRegions("bg_bird");
		this.crane = Resource.findRegion("bg_crane");
		this.tunnel = Resource.findRegion("bg_train_tunnel");

		this.birdAnimation = new Animation(0.2f, this.bird);
		
		this.initClouds();

	}
	
	protected void initClouds(){
		//init Cloud static attributes
		Cloud.textureRegions = Resource.findRegions("bg_cloud");
		Cloud.minSpeed = 5f;
		Cloud.maxSpeed = 10f;
		Cloud.yUpperBound = this.getHeight()-20f;
		Cloud.yLowerBound = 0f;
		Cloud.maxLighten = 0f;
		
		this.clouds = new ArrayList<Cloud>();
		Random rand = new Random();
		for (int i = 0; i < 2; i++) {
			this.clouds.add(new Cloud(rand.nextFloat()*this.getWidth()));
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		for (Cloud cloud : clouds) {
			if(cloud.getX() <= this.getWidth())
				cloud.act(delta);
			else
				cloud.reset();
		}
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		this.stateTime += Gdx.graphics.getDeltaTime();

		drawSky(batch);
		drawClouds(batch);
		drawBirds(batch);
		drawHarborPlatform(batch);
		drawCranes(batch);
		drawTunnel(batch);
	}
	
	protected void drawSky(Batch batch){
		drawSky(batch, SKY_COLOR);
	}
	
	protected void drawSky(Batch batch, Color color){
		batch.end();
		shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color);
		shapeRenderer.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		shapeRenderer.end();
		batch.begin();
	}
	
	protected void drawCloud(Batch batch, Color tint){
		batch.setColor(tint);
		drawClouds(batch);
		batch.setColor(Color.WHITE);
	}
	
	protected void drawClouds(Batch batch){
		Color originalColor = batch.getColor();
		for (Cloud cloud : clouds) {
			if(cloud.getLighten() > 0){
				Color actualColor = originalColor.cpy();
				float v = cloud.getLighten()/255f;
				actualColor.add(v, v, v, 1f);
				batch.setColor(actualColor);
			}
			batch.draw(cloud.getTexture(), cloud.getX(), cloud.getY());
		}
		batch.setColor(originalColor);
	}
	
	protected void drawBirds(Batch batch, Color tint){
		batch.setColor(tint);
		drawBirds(batch);
		batch.setColor(Color.WHITE);
	}
	
	protected void drawBirds(Batch batch){
		batch.draw(
				this.birdAnimation.getKeyFrame(stateTime, true),
				this.getWidth() / 4,
				this.getHeight() / 2);

		batch.draw(
				this.birdAnimation.getKeyFrame(stateTime, true),
				this.getWidth() / 4.5f,
				this.getHeight() / 1.5f);
	}
	
	protected void drawHarborPlatform(Batch batch){
		drawHarborPlatform(batch, HARBOR_COLOR);
	}
	
	protected void drawHarborPlatform(Batch batch, Color color){
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color);
		shapeRenderer.rect(this.getX(), this.getY(), this.getWidth(), this.harborLevel);
		shapeRenderer.end();
		batch.begin();
	}
	
	protected void drawCranes(Batch batch, Color tint){
		batch.setColor(tint);
		drawClouds(batch);
		batch.setColor(Color.WHITE);
	}

	protected void drawCranes(Batch batch){
		for(int i=0;i < 3;i++){
			batch.draw(
					this.crane,
					5+(this.crane.getRegionWidth()+20)*i,
					this.getX()+this.harborLevel);
		}
		batch.setColor(Color.WHITE);
	}
	
	protected void drawTunnel(Batch batch){		
		for(float i=0;i<this.getWidth()+this.tunnel.getRegionWidth();i+=this.tunnel.getRegionWidth()){
			batch.draw(
					this.tunnel,
					i,
					this.getHeight()-this.tunnel.getRegionHeight());
		}
	}

	@Override
	public float getWidth(){
		return stage.getWidth();
	}
	
	@Override
	public float getHeight(){
		return stage.getHeight();
	}
	
	public void setStage(Stage stage){
		this.stage = stage;
	}
	
	protected static class Cloud {
		public static Array<AtlasRegion> textureRegions;
		public static float yUpperBound;
		public static float yLowerBound;
		public static float minSpeed;
		public static float maxSpeed;
		public static float maxLighten;
		
		private int regionIndex;
		private Vector2 position;
		private float speed;
		private float lighten;
		
		public Cloud(){
			this.position = new Vector2();
			
			this.reset();
		}
		
		public Cloud(float initX){
			this();
			this.position.x = initX;
		}
		
		public void reset(){
			Random rand = new Random();
			this.regionIndex = rand.nextInt(getTextureRegions().size);
			this.position = new Vector2(
					(-1)*getTextureRegions().get(this.regionIndex).getRegionWidth(), 
					yLowerBound + rand.nextFloat()*(yUpperBound-yLowerBound));
			this.speed = minSpeed + rand.nextFloat()*(maxSpeed-minSpeed);
			this.lighten = rand.nextFloat()*maxLighten;
		}
		
		public void act(float delta){
			this.position.add(this.speed*delta, 0);
		}
		
		public TextureRegion getTexture(){
			return getTextureRegions().get(regionIndex);
		}
		
		public float getX(){
			return position.x;
		}
		
		public float getY(){
			return position.y;
		}

		public static Array<AtlasRegion> getTextureRegions() {
			return textureRegions;
		}

		public static void setTextureRegions(Array<AtlasRegion> textureRegions) {
			Cloud.textureRegions = textureRegions;
		}

		public float getLighten() {
			return lighten;
		}
	}
}

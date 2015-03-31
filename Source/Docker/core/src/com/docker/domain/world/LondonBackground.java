package com.docker.domain.world;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.docker.technicalservices.Resource;

public class LondonBackground extends Background {

	private static final Color BRIDGE_COLOR = Color.valueOf("788b97");
	private static final Color SKYLINE_COLOR = Color.valueOf("92a3ac");
	private static final Color SKY_COLOR = Color.valueOf("c2c8ca");
	private static final Color CLOUD_COLOR = Color.valueOf("80878b");
	private static final Color BIRD_COLOR = Color.valueOf("303030");
	
	private TextureRegion towerBridge;
	private TextureRegion skyline;
	private TextureRegion stPauls;
	private TextureRegion gherkin;
	
	public LondonBackground(float width, float height) {
		super(width, height);
		
		this.towerBridge = Resource.findRegion("london_tower_bridge");
		this.skyline = Resource.findRegion("london_skyline");
		this.stPauls = Resource.findRegion("london_st_pauls");
		this.gherkin = Resource.findRegion("london_gherkin");
		
		this.harborLevel = 30f;
	}
	
	protected void initClouds(){
		//init Cloud static attributes
		Cloud.textureRegions = Resource.findRegions("bg_cloud");
		Cloud.minSpeed = 5f;
		Cloud.maxSpeed = 30f;
		Cloud.yUpperBound = this.getHeight()-40f;
		Cloud.yLowerBound = this.getHeight()/2f-20f;
		Cloud.maxLighten = 50f;
		
		this.clouds = new ArrayList<Cloud>();
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			this.clouds.add(new Cloud(rand.nextFloat()*this.getWidth()));
		}
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		this.stateTime += Gdx.graphics.getDeltaTime();

		drawSky(batch, SKY_COLOR);
		drawCloud(batch, CLOUD_COLOR);
		drawBirds(batch, BIRD_COLOR);
		drawHarborPlatform(batch, SKYLINE_COLOR);
		drawLondon(batch);
		drawBridgePlatform(batch, BRIDGE_COLOR);
		//drawCranes(batch);	
		drawTunnel(batch);	
	}
	
	protected void drawLondon(Batch batch){
		
		int skylineCount = (int) Math.ceil(this.getWidth() / skyline.getRegionWidth());
		for (int i = 0; i < skylineCount; i++) {
			batch.draw(skyline, skyline.getRegionWidth()*i, harborLevel);
		}
		
		batch.draw(this.stPauls, this.getWidth() - 200f, harborLevel);
		batch.draw(this.gherkin, this.getWidth() - 100f, harborLevel);		
		batch.draw(this.towerBridge, 0f, 20f);
	}
	
	protected void drawBridgePlatform(Batch batch, Color color){
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color);
		shapeRenderer.rect(
				this.towerBridge.getRegionWidth(),
				this.getY(), 
				this.getWidth(),
				20+14
				);
		shapeRenderer.end();
		batch.begin();
	}
}
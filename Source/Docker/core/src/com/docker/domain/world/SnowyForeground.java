package com.docker.domain.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.docker.technicalservices.Resource;

public class SnowyForeground extends Foreground{

	List<SnowParticle> snowParticles;
	Color waterColor = Color.valueOf("d5ebf2");
	Color waterBorderColor = Color.valueOf("a5bfc7");
	
	public SnowyForeground(Stage stage) {
		this(stage, DEFAULT_WATERLEVEL);
	}
	
	public SnowyForeground(Stage stage, float waterlevel){
		super(stage, waterlevel);
		
		this.snowParticles = new ArrayList<SnowParticle>();
		Random rand = new Random();
		for (int i = 0; i < 100; i++) {
			SnowParticle particle = new SnowParticle(
					rand.nextFloat()*stage.getWidth()-10, 
					rand.nextFloat()*stage.getHeight(), 
					0, 0
					);
			particle.velocity.set(0, rand.nextFloat()*-10);
			this.snowParticles.add(particle);
		}
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		
		for (Iterator<SnowParticle> iterator = snowParticles.iterator(); iterator.hasNext();) {
			SnowParticle snowParticle = (SnowParticle) iterator.next();
			if(snowParticle.position.y + snowParticle.height <= 0){
				Random rand = new Random();
				snowParticle.position.set(
						rand.nextFloat()*getStage().getWidth(),
						getStage().getHeight() + snowParticle.height);
				snowParticle.velocity.set(0, rand.nextFloat()*-10);
			}
			snowParticle.act(delta);
		}
		
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) {
		drawDock(batch);
		drawWaterLevel(batch);
		drawWaterPlane(batch, waterColor, waterBorderColor);
		drawWaterAnimation(batch, waterColor);
		drawSnow(batch);
		drawHud(batch);
	}
	
	public void drawSnow(Batch batch){
		for (Iterator<SnowParticle> iterator = snowParticles.iterator(); iterator.hasNext();) {
			SnowParticle snowParticle = (SnowParticle) iterator.next();
			snowParticle.draw(batch);
		}
	}


	private class SnowParticle{
		private static final float GRAVITY = -30;
		
		private Color color = new Color(0.93f, 1, 1, 0.95f);
		private Vector2 position;
		private Vector2 velocity;
		private float height = 5;
		private float width = 5;
		private float statetime;
		private float freq;
		private float amp;
		
		public SnowParticle(float x, float y, float vx, float vy){
			this.position = new Vector2(x, y);
			this.velocity = new Vector2(vx, vy);
			Random rand = new Random();
			stateTime = rand.nextFloat();
			this.width = this.height = 2 + rand.nextFloat()*4;
			this.freq = 2 + rand.nextFloat()*3;
			this.amp = 10+rand.nextFloat()*20;
		}
		
		public void act(float delta){
			statetime += delta;
			position.add((float)Math.sin(statetime*freq)*amp*delta, (velocity.y + GRAVITY)*delta);
			//position.add(velocity.cpy().scl(delta));
			//velocity = velocity.add(0, GRAVITY*delta);
		}
		
		public void draw(Batch batch){
			if(color.a > 0){
				batch.setColor(color);
				batch.draw(Resource.getSmokePuffTexture(), position.x-width/2, position.y-height/2, width, height);
				batch.setColor(Color.WHITE);
				batch.draw(Resource.getSmokePuffTexture(), position.x-width/4, position.y-height/4, width/2, height/2);
				batch.setColor(Color.WHITE);
			}
		}
	}
}

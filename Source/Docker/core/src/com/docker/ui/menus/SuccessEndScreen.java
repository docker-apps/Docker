package com.docker.ui.menus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.game.AbstractGame;
import com.docker.technicalservices.Resource;

public class SuccessEndScreen extends AbstractMenu{

	protected Label title = new Label("Success!", skin, "title-white");
	protected Label text;
	protected Button homeButton = createHomeButton(skin);
	protected Button retryButton = createRetryButton(skin);
	
	protected List<Particle> particles;

	public SuccessEndScreen(final Docker application, TextureRegion background, final AbstractGame game, int gameScore, int highscore) {
		super(application, background);

		homeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				application.showAds(true);
				application.returnToMenu();
			}
		});
		retryButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.startNewGame();
			}
		});

		text = new Label(
				"Your Score: " + gameScore +
				"\nHighscore: " + highscore, 
				skin);

		table.add(title).padBottom(10).colspan(3).left().row();
		table.add(text).colspan(3).left().row();
		table.add(homeButton).size(100, 40).padTop(10).center();
		table.add(retryButton).size(100, 40).padTop(10).padLeft(5).center();
		
		this.particles = new ArrayList<SuccessEndScreen.Particle>();
		generateParticles(300, (float)Math.toRadians(60), (float)Math.toRadians(20), 100, 75, 0, 0);
		generateParticles(300, (float)Math.toRadians(120), (float)Math.toRadians(20), 100, 75, stage.getWidth(), 0);
	}
	
	private void generateParticles(int count, float angle, float angleVar, float speed, float speedVar, float x, float y){
		Random rand = new Random();
		for (int i = 0; i < count; i++) {
			float curAngle = (float)rand.nextGaussian()*angleVar + angle;
			float curSpeed = (float)rand.nextGaussian()*speedVar + speed;
			float vx = (float)Math.cos(curAngle)*curSpeed;
			float vy = (float)Math.sin(curAngle)*curSpeed;
			this.particles.add(new Particle(x, y, vx, vy));
		}
		
	}
	
	@Override
    public void lastScreenOnReturn(){
    	if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
			application.showAds(true);
			application.returnToMenu();
        }
    }
	
	@Override
	public void render(float delta){
		super.render(delta);
		
		for (Iterator<Particle> iterator = particles.iterator(); iterator.hasNext();) {
			Particle particle = (Particle) iterator.next();
			if(particle.color.a > 0){
				particle.act(delta);
				Batch batch = stage.getBatch();
				batch.begin();
				particle.draw(batch);
				batch.end();
			}
			else{
				iterator.remove();
			}
			
		}
	}

	private class Particle {
		private static final float GRAVITY = -150;
		
		private Color color = new Color(1, 1, 1, 0.8f);
		private Vector2 position;
		private Vector2 velocity;
		private float height = 5;
		private float width = 5;
		
		public Particle(float x, float y, float vx, float vy){
			this.position = new Vector2(x, y);
			this.velocity = new Vector2(vx, vy);
		}
		
		public void act(float delta){
			position.add(velocity.cpy().scl(delta));
			velocity = velocity.add(0, GRAVITY*delta);
			if(color.a > 0)
				color.a -= delta/5;
				this.height = 2/(color.a + 0.1f);
				this.width = 2/(color.a + 0.1f);
		}
		
		public void draw(Batch batch){
			if(color.a > 0){
				batch.setColor(color);
				batch.draw(Resource.getSmokePuffTexture(), position.x-width/2, position.y-height/2, width, height);
				batch.draw(Resource.getSmokePuffTexture(), position.x-width/4, position.y-height/4, width/2, height/2);
				batch.setColor(Color.WHITE);
			}
		}
		
	}
}

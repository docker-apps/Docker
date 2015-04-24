package com.docker.domain.world;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.docker.technicalservices.Resource;

public class RainyForeground extends Foreground {
	private static int RAIN_DROP_AMOUNT = 50;
	
	public RainyForeground(Stage stage) {
		this(stage, DEFAULT_WATERLEVEL);
		ambientSound.add(Resource.getRainAmbient());
	}


	public RainyForeground(Stage stage, float waterLevel) {
		super(stage, waterLevel);

	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		drawRain(batch);
	}

	private void drawRain(Batch batch){
		//draw raindrop
		Random rand = new Random();
		for (int i = 0; i < RAIN_DROP_AMOUNT; i++) {
			batch.draw(
					Resource.getRainTexture(), 
					rand.nextFloat()*getStage().getWidth(), 
					rand.nextFloat()*getStage().getHeight());
			if(rand.nextFloat() <= 0.3f){
				batch.draw(
						this.waterMovementAnimation.getKeyFrame(stateTime, true),
						rand.nextFloat()*this.getWidth(),
						rand.nextFloat()*this.getWaterLevel()-2);

			}
		}
	}
}

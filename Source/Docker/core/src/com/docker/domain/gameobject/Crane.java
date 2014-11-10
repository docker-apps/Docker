package com.docker.domain.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class Crane extends Actor {
	private int speed;
	private float xOrigin;
	private float yOrigin;
	private Container container;

	private TextureRegion body;
	private TextureRegion extensionLeft;
	private TextureRegion extensionLeftOuter;
	private TextureRegion extensionRight;
	private TextureRegion extensionRightOuter;
	private TextureRegion cable;
	
	/**
	 * @param speed the speed at which the crane can move.
	 * @param xOrigin the crane's initial position on the x-plane. The Crane will always return to this position.
	 * @param yOrigin the crane's initial position on the y-plane. The Crane will always return to this position.
	 */
	public Crane(int speed, float xOrigin, float yOrigin){
		super();
		this.setX(xOrigin);
		this.setY(yOrigin);
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
		this.speed = speed;
		this.container = null;

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));
		this.body = atlas.findRegion("crane_body");
		this.extensionLeft = atlas.findRegion("crane_extension_left");
		this.extensionLeftOuter = atlas.findRegion("crane_extension_left_outer");
		this.extensionRight = atlas.findRegion("crane_extension_right");
		this.extensionRightOuter = atlas.findRegion("crane_extension_right_outer");
		this.cable = atlas.findRegion("crane_cable");
	}
	
	/**
	 * Orders the crane to deploy the given container to the ship, at the given position.
	 * 
	 * @param container the container to deploy
	 * @param ship the ship to which the container should be deployed
	 * @param x the position on the ship at which the container should be deployed
	 */
	public void deployContainer(Container container, final Ship ship, final float x, float y){
		// add the container to the crane
		this.setContainer(container);
		
		// calculate the animation duration from the distance to the target and the cranes speed
		float xDistance = Math.abs(this.getX() - x);
		float yDistance = Math.abs(this.getY() - y);
		float distance = (float) Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
		float duration = distance / this.speed;
		
		// create a new Move-To Action
		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(x, y);
		moveAction.setDuration(duration);
		
		// create an action which deploys the container to the ship
		Action completeAction = new Action(){
		    public boolean act( float delta ) {
		        // give the container to the ship here, preferrably to a grid coordinate
		    	Container container = removeContainer();
		    	ship.addContainer(x, container);
		    	setX(getX()+container.getWidth()/2);
		    	setY(getY()+container.getElementHeight());
		    	return true;
		    }
		};
		
		// create a new Move-To Action
		MoveToAction moveBackAction = new MoveToAction();
		moveBackAction.setPosition(xOrigin, yOrigin);
		moveBackAction.setDuration(1);
		
		// chain the two actions and add it to this actor
		SequenceAction actions = new SequenceAction(moveAction, completeAction, moveBackAction);
		this.addAction(actions);
	}
	
	public boolean isDeploying(){
		return this.getActions().size > 0;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if(this.container != null){
			this.container.setX(this.getX());
			this.container.setY(this.getY());			
		}
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		float xBody;
		float yBody;
		if(this.container != null){
			int length = this.container.getLength();
			float elementWidth = this.container.getElementWidth();
			float yPos = this.getY()+this.container.getHeight()-4;

			this.container.draw(batch, parentAlpha);
			
			// Not great code, but it works. If you can create an elegant algorithm, feel free to improve.
			if(length == 5){
				batch.draw(this.extensionLeftOuter,
						this.getX(),
						yPos);
				batch.draw(this.extensionRightOuter,
						this.getX()+elementWidth*4,
						yPos);
				batch.draw(this.extensionLeft,
						this.getX()+elementWidth,
						yPos);
				batch.draw(this.extensionRight,
						this.getX()+elementWidth*3,
						yPos);
			}
			else if(length == 4){
				batch.draw(this.extensionLeftOuter,
						this.getX(),
						yPos);
				batch.draw(this.extensionRightOuter,
						this.getX()+elementWidth*3,
						yPos);
				batch.draw(this.extensionLeft,
						this.getX()+elementWidth*0.5f,
						yPos);
				batch.draw(this.extensionRight,
						this.getX()+elementWidth*2f,
						yPos);
			}
			else if(length == 3){
				batch.draw(this.extensionLeft,
						this.getX(),
						yPos);
				batch.draw(this.extensionRight,
						this.getX()+elementWidth*2f,
						yPos);
			}
			else if(length == 2){
				batch.draw(this.extensionLeft,
						this.getX(),
						yPos);
				batch.draw(this.extensionRight,
						this.getX()+elementWidth,
						yPos);
			}



			xBody = this.getX()+((float)this.container.getWidth())/2-((float)this.body.getRegionWidth())/2;
			yBody = this.getY()+this.container.getHeight()-4;
		}
		else{
			xBody = this.getX();
			yBody = this.getY();
		}
		batch.draw(this.body, xBody, yBody);
		
		for(float i=yBody + this.body.getRegionHeight();i<this.getStage().getHeight()+this.cable.getRegionHeight();i+=this.cable.getRegionHeight()){
			batch.draw(this.cable, xBody+8, i);
		}
	}

	/**
	 * @param container the container which the crane holds.
	 */
	public void setContainer(Container container){
		this.container = container;
	}
	
	/**
	 * @return wether the crane is holding a container.
	 */
	public boolean hasContainer(){
		return this.container != null;
	}

	/** 
	 * Removes the container which the crane is holding and returns it.
	 * 
	 * @return the container which the crane was holding, null if it's not holding one.
	 */
	public Container removeContainer(){
		Container container = this.container;
		this.container = null;
		return container;
	}
}

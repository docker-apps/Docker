package com.docker.domain.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

/**
 * @author HAL9000
 *	
 * A container. In Docker, everything is about containers.
 */
public class Container extends Actor {

	private static final float LABEL_TRANSPARENCY = 0.8f;

	/**
	 * The Color Value to tint red Containers with. Always use these Colors for Containers.
	 */
	public final static Color RED = new Color(1f, 0.2f, 0.15f, 1f);
	/**
	 * The Color Value to tint green Containers with. Always use these Colors for Containers.
	 */
	public final static Color GREEN = new Color(0.5f, 1f, 0.15f, 1f);
	/**
	 * The Color Value to tint blue Containers with. Always use these Colors for Containers.
	 */
	public final static Color BLUE = new Color(0.15f, 0.5f, 1f, 1f);
	/**
	 * The Color Value to tint yellow Containers with. Always use these Colors for Containers.
	 */
	public final static Color YELLOW = new Color(1f, 1f, 0f, 1f);

	private int weight;
	private int length;

	private TextureRegion baseLeft;
	private TextureRegion baseCenter;
	private TextureRegion baseRight;
	private TextureRegion baseFront;
	private TextureRegion number;
	private TextureRegion label;
	

	/**
	 * @param weight the container's weight value.
	 * @param length the container's length (not in pixels but in amount of elements)
	 * @param color the container's color. Any color is possible, but you should stick to the ones defined in this class.
	 */
	public Container(int weight, int length, Color color) {
		super();
		
		if(length <= 0)
			throw new IllegalArgumentException();		
		
		this.weight = weight;
		this.length = length;
		this.setColor(color);
		this.setX(0);
		this.setY(0);
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));		
		this.baseLeft= atlas.findRegion("container_base_left");
		this.baseCenter = atlas.findRegion("container_base_center");
		this.baseRight = atlas.findRegion("container_base_right");
		this.baseFront = atlas.findRegion("container_base_front");
		this.number = atlas.findRegions("nr").get(this.weight-1);
		this.label = atlas.findRegions("label").get(this.length > 1 ? 0 : 1);
		
		this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	/**
	 * @param weight the container's weight value.
	 * @param length the container's length (not in pixels but in amount of elements)
	 * @param color the container's color. Any color is possible, but you should stick to the ones defined in this class.
	 * @param x the container's initial position in the x-plane
	 * @param y the container's initial position in the y-plane
	 */
	public Container(int weight, int length, Color color, float x, float y) {
       this(weight,length,color);
       this.setX(x);
       this.setY(y);
	}
	
	/**
	 * @param weight the container's weight value.
	 * @param length the container's length (not in pixels but in amount of elements)
	 */
	public Container(int weight, int length){
		this(weight, length, getRandomColor());
	}

	/**
	 * Create a new Container from an existing container.
	 * All attributes, as the weight, length, color and position will be copied.
	 * 
	 * @param container the container on which to base the new container on.
	 */
	public Container(Container container) {
		this(container.getWeight(), container.getLength(), container.getColor(), container.getX(), container.getY());
	}

	/**
	 * Creates a destroying (explosion) animation at the containers position.
	 * 
	 * @param stage the stage in which the explosion should be shown.
	 */
	public void destroy(Stage stage){
		for (int i = 0; i < this.length; i++) {
			ContainerExplosion explosion = new ContainerExplosion(this.getX()+i*this.getElementWidth(), this.getY());
			stage.addActor(explosion);
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		
		//draw container base
		batch.setColor(this.getColor());
		if(this.length > 1){
			batch.draw(this.baseLeft, this.getX(), this.getY());
			for (int i = 1; i <= this.length-2; i++) {
				batch.draw(this.baseCenter, 
						this.getX()+(getElementWidth()*i), 
						this.getY());			
			}
			batch.draw(this.baseRight, 
					this.getX()+(getElementWidth()*(this.length-1)), 
					this.getY());
		}
		else{
			batch.draw(this.baseFront, 
					this.getX(), 
					this.getY());
		}
		//draw number & labels
		batch.setColor(new Color(1f,1f,1f,LABEL_TRANSPARENCY));
		batch.draw(this.number, 
				this.getX()+this.getWidth()-this.number.getRegionWidth()-3, 
				this.getY()+this.getElementHeight()-this.number.getRegionHeight()-3);

		batch.draw(label, 
				this.getX()+3, 
				this.getY()+3);

		//reset color tint to white
		batch.setColor(Color.WHITE);
	}

	@Override
	public float getWidth(){
		return this.length * this.getElementWidth();
	}

	@Override
	public float getHeight(){
		return this.getElementHeight();
	}
	
	/**
	 * @return the container's length in amount of elements.
	 */
	public int getLength(){
		return this.length;
	}

	/**
	 * @return the width of an individual element.
	 */
	public float getElementWidth(){
		return baseLeft.getRegionWidth();
	}

	/**
	 * @return the height of an individual element.
	 */
	public float getElementHeight(){
		return baseLeft.getRegionHeight();
	}
	/**
	 * @return the weight of the container
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight of the container
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	private static Color getRandomColor() {
		int randomColorNr = (int)(Math.random()*4);
		switch (randomColorNr) {
		case 0:
			return RED;
		case 1:
			return GREEN;
		case 2:
			return BLUE;
		default:
			return YELLOW;
		}
	}
	
	private class ContainerExplosion extends Actor{
		private Array<AtlasRegion> explosion;
		private Animation explosionAnimation;
		private float stateTime;
		
		public ContainerExplosion(float x, float y){
			this.setX(x);
			this.setY(y);
			this.stateTime = 0f;
			
			TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));	
			this.explosion = atlas.findRegions("container_explosion");
			this.explosionAnimation = new Animation(0.05f, explosion);

		}
		
		@Override
		public void act(float delta){
			if(this.explosionAnimation.isAnimationFinished(stateTime))
				this.remove();
		}
		
		@Override
		public void draw(Batch batch, float delta){
			this.stateTime += Gdx.graphics.getDeltaTime();
			batch.draw(this.explosionAnimation.getKeyFrame(stateTime), this.getX(), this.getY());
		}
	}
}

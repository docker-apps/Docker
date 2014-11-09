package com.docker.domain.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Container extends Actor {

	private static final float LABEL_TRANSPARENCY = 0.8f;

	public final static Color RED = new Color(1f, 0.2f, 0.15f, 1f);
	public final static Color GREEN = new Color(0.5f, 1f, 0.15f, 1f);
	public final static Color BLUE = new Color(0.15f, 0.5f, 1f, 1f);
	public final static Color YELLOW = new Color(1f, 1f, 0f, 1f);

	private int weight;
	private int length;
	private Color color;

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
	 * @param x the container's initial position in the x-plane
	 * @param y the container's initial position in the y-plane
	 */
	public Container(int weight, int length, Color color, float x, float y) {
		super();
		
		if(length <= 0)
			throw new IllegalArgumentException();		
		
		this.setX(x);
		this.setY(y);
		this.weight = weight;
		this.length = length;
		this.color = color;
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));		
		this.baseLeft= atlas.findRegion("container_base_left");
		this.baseCenter = atlas.findRegion("container_base_center");
		this.baseRight = atlas.findRegion("container_base_right");
		this.baseFront = atlas.findRegion("container_base_front");
		this.number = atlas.findRegions("nr").get(this.weight-1);
		this.label = atlas.findRegions("label").get(this.length > 1 ? 0 : 1);

		this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	public Container(int weight, int length, float x, float y){
		this(weight, length, RED, x, y);
		this.setRandomColor();
	}



	public Container(Container container) {
		this(container.getWeight(), container.getLength(), container.getColor(), container.getX(), container.getY());
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		//draw container base
		batch.setColor(this.color);
		if(this.length > 1){
			batch.draw(this.baseLeft, this.getX(), this.getY());
			for (int i = 1; i <= this.length-2; i++) {
				batch.draw(this.baseCenter, this.getX()+(getElementWidth()*i), this.getY());			
			}
			batch.draw(this.baseRight, this.getX()+(getElementWidth()*(this.length-1)), this.getY());
		}
		else{
			batch.draw(this.baseFront, this.getX(), this.getY());
		}
		//draw number & labels
		batch.setColor(new Color(1f,1f,1f,LABEL_TRANSPARENCY));
		batch.draw(this.number, this.getX()+3, this.getY()+this.getElementHeight()-this.number.getRegionHeight()-3);

		batch.draw(label, this.getX()+(getElementWidth()*(this.length)) - label.getRegionWidth() - 3, this.getY() + 3);

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
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	private void setRandomColor() {
		int randomColorNr = (int)Math.random()*4;
		switch (randomColorNr) {
		case 0:
			this.color = RED;
			break;
		case 1:
			this.color = GREEN;
			break;
		case 2:
			this.color = BLUE;
		default:
			this.color = YELLOW;
			break;
		}
	}
}

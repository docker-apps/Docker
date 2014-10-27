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

	public enum ContainerColor {
		RED, GREEN, BLUE, YELLOW
	}

	public final static Color RED = new Color(1f, 0.2f, 0.15f, 1f);
	public final static Color GREEN = new Color(0.5f, 1f, 0.15f, 1f);
	public final static Color BLUE = new Color(0.15f, 0.5f, 1f, 1f);
	public final static Color YELLOW = new Color(1f, 1f, 0f, 1f);

	private int weight;
	private int length;
	private Color color;

	private TextureRegion base_left;
	private TextureRegion base_center;
	private TextureRegion base_right;
	private TextureRegion base_front;
	private TextureRegion number;
	private TextureRegion label;

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
		this.base_left= atlas.findRegion("container_base_left");
		this.base_center = atlas.findRegion("container_base_center");
		this.base_right = atlas.findRegion("container_base_right");
		this.base_front = atlas.findRegion("container_base_front");
		this.number = atlas.findRegions("nr").get(this.weight-1);
		this.label = atlas.findRegions("label").get(this.length > 1 ? 0 : 1);

		this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
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
			batch.draw(this.base_left, this.getX(), this.getY());
			for (int i = 1; i <= this.length-2; i++) {
				batch.draw(this.base_center, this.getX()+(getElementWidth()*i), this.getY());			
			}
			batch.draw(this.base_right, this.getX()+(getElementWidth()*(this.length-1)), this.getY());
		}
		else{
			batch.draw(this.base_front, this.getX(), this.getY());
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

	public float getElementWidth(){
		return base_left.getRegionWidth();
	}

	public float getElementHeight(){
		return base_left.getRegionHeight();
	}
}

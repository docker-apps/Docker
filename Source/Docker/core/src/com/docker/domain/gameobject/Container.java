package com.docker.domain.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Container extends Actor {

	public enum ContainerColor {
		RED, GREEN, BLUE, YELLOW
	}

	public final static Color RED = new Color(1f, 0.2f, 0.15f, 1f);

	private int weight;
	private int length;
	private Color color;

	private Texture container_base_left;
	private Texture container_base_center;
	private Texture container_base_right;
	private Texture container_base_front;
	private Texture number;
	private Texture label;

	public Container(int weight, int length, Color color, float x, float y) {
		super();
		
		if(length <= 0)
			throw new IllegalArgumentException();		
		
		this.setX(x);
		this.setY(y);
		this.weight = weight;
		this.length = length;
		this.color = color;

		this.container_base_left= new Texture(Gdx.files.internal("img//container_base_left.png"));
		this.container_base_center = new Texture(Gdx.files.internal("img//container_base_center.png"));
		this.container_base_right = new Texture(Gdx.files.internal("img//container_base_right.png"));
		this.container_base_front = new Texture(Gdx.files.internal("img//container_base_front.png"));
		this.number = new Texture(Gdx.files.internal("img//nr_"+this.weight+".png"));
		if(this.length > 1)
			this.label = new Texture(Gdx.files.internal("img//label_1.png"));
		else
			this.label = new Texture(Gdx.files.internal("img//label_2.png"));

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
			batch.draw(this.container_base_left, this.getX(), this.getY());
			for (int i = 1; i <= this.length-2; i++) {
				batch.draw(this.container_base_center, this.getX()+(getElementWidth()*i), this.getY());			
			}
			batch.draw(this.container_base_right, this.getX()+(getElementWidth()*(this.length-1)), this.getY());
		}
		else{
			batch.draw(this.container_base_front, this.getX(), this.getY());
		}
		//draw number & labels, set transparency here
		batch.setColor(new Color(1f,1f,1f,0.8f));
		batch.draw(this.number, this.getX()+3, this.getY()+this.getElementHeight()-this.number.getHeight()-3);

		batch.draw(label, this.getX()+(getElementWidth()*(this.length)) - label.getWidth() - 3, this.getY() + 3);

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
		return container_base_left.getWidth();
	}

	public float getElementHeight(){
		return container_base_left.getHeight();
	}
}

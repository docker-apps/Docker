package com.docker.domain.gameobject.shipskins;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public interface IShipSkin {	
	public TextureRegion getBodyLeft();
	public TextureRegion getBodyCenter();
	public TextureRegion getBodyRight();
	public TextureRegion getTower();
	public TextureRegion getMast();
	public TextureRegion getIndicatorLamp();
	public TextureRegion getBodyBrokenLeft();
	public TextureRegion getBodyBrokenRight();
	
	public Vector2 getMastOffset();
	public Vector2 getTowerOffset();
}

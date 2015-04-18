package com.docker.domain.gameobject.shipskins;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.docker.technicalservices.Resource;

public class DefaultShipSkin implements IShipSkin{
	
	protected TextureRegion bodyLeft;
	protected TextureRegion bodyCenter;
	protected TextureRegion bodyRight;
	protected TextureRegion tower;
	protected TextureRegion mast;
	protected TextureRegion indicatorLampOn;
	protected TextureRegion bodyBrokenLeft;
	protected TextureRegion bodyBrokenRight;
	
	public DefaultShipSkin(){
		this.bodyLeft = Resource.findRegion("ship_body_left");
		this.bodyCenter = Resource.findRegion("ship_body_center");
		this.bodyRight = Resource.findRegion("ship_body_right");
		this.tower = Resource.findRegion("ship_tower");
		this.mast = Resource.findRegion("ship_mast");
		this.indicatorLampOn = Resource.findRegion("indicator_lamp_on");
		this.bodyBrokenLeft = Resource.findRegion("ship_body_broken_left");
		this.bodyBrokenRight = Resource.findRegion("ship_body_broken_right");
	}

	@Override
	public TextureRegion getBodyLeft() {
		return bodyLeft;
	}

	@Override
	public TextureRegion getBodyCenter() {
		return bodyCenter;
	}

	@Override
	public TextureRegion getBodyRight() {
		return bodyRight;
	}

	@Override
	public TextureRegion getTower() {
		return tower;
	}

	@Override
	public TextureRegion getMast() {
		return mast;
	}

	@Override
	public TextureRegion getIndicatorLamp() {
		return indicatorLampOn;
	}

	@Override
	public TextureRegion getBodyBrokenLeft() {
		return bodyBrokenLeft;
	}

	@Override
	public TextureRegion getBodyBrokenRight() {
		return bodyBrokenRight;
	}

	@Override
	public Vector2 getMastOffset() {
		return new Vector2(21, 0);
	}

	@Override
	public Vector2 getTowerOffset() {
		return new Vector2(-1, 0);
	}
}

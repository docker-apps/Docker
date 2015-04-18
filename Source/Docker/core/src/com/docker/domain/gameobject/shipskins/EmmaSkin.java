package com.docker.domain.gameobject.shipskins;

import com.badlogic.gdx.math.Vector2;
import com.docker.technicalservices.Resource;

public class EmmaSkin extends DefaultShipSkin {
	public EmmaSkin(){
		this.bodyLeft = Resource.findRegion("emma_body_left");
		this.bodyCenter = Resource.findRegion("emma_body_center");
		this.bodyRight = Resource.findRegion("emma_body_right");
		this.tower = Resource.findRegion("emma_tower");
		this.mast = Resource.findRegion("emma_mast");
		this.indicatorLampOn = Resource.findRegion("indicator_lamp_on");
		this.bodyBrokenLeft = Resource.findRegion("emma_body_broken_left");
		this.bodyBrokenRight = Resource.findRegion("emma_body_broken_right");
	}
	
	@Override
	public Vector2 getMastOffset(){
		return new Vector2(5, -3);
	}

	@Override
	public Vector2 getTowerOffset() {
		return new Vector2(-12, 0);
	}
}

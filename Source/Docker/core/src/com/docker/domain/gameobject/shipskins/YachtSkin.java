package com.docker.domain.gameobject.shipskins;

import com.badlogic.gdx.math.Vector2;
import com.docker.technicalservices.Resource;

public class YachtSkin extends DefaultShipSkin {
	public YachtSkin(){
		this.bodyLeft = Resource.findRegion("yacht_body_left");
		this.bodyCenter = Resource.findRegion("yacht_body_center");
		this.bodyRight = Resource.findRegion("yacht_body_right");
		this.tower = Resource.findRegion("yacht_tower");
		this.mast = Resource.findRegion("yacht_mast");
		this.indicatorLampOn = Resource.findRegion("indicator_lamp_on");
		this.bodyBrokenLeft = Resource.findRegion("yacht_body_broken_left");
		this.bodyBrokenRight = Resource.findRegion("yacht_body_broken_right");
	}
	
	@Override
	public Vector2 getMastOffset() {
		return new Vector2(18, 0);
	}
}

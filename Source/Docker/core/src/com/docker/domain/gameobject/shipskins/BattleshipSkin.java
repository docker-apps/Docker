package com.docker.domain.gameobject.shipskins;

import com.docker.technicalservices.Resource;

public class BattleshipSkin extends DefaultShipSkin{
	public BattleshipSkin(){
		this.bodyLeft = Resource.findRegion("battleship_body_left");
		this.bodyCenter = Resource.findRegion("battleship_body_center");
		this.bodyRight = Resource.findRegion("battleship_body_right");
		this.tower = Resource.findRegion("battleship_tower");
		this.mast = Resource.findRegion("battleship_mast");
		this.indicatorLampOn = Resource.findRegion("indicator_lamp_on");
		this.bodyBrokenLeft = Resource.findRegion("battleship_body_broken_left");
		this.bodyBrokenRight = Resource.findRegion("battleship_body_broken_right");
	}
}

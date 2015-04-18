package com.docker.domain.gameobject.shipskins;

import com.docker.Docker;
import com.docker.technicalservices.Persistence;

public class ShipSkinManager {
	public static final int DEFAULT_SHIP_SKIN = 0;
	public static final int YACHT_SHIP_SKIN = 1;
	public static final int EMMA_SHIP_SKIN = 2;
	public static final int BATTLE_SHIP_SKIN = 3;

	public static IShipSkin getConfiguredSkin(){
		return getSkin(Persistence.getShipSkinId());
	}

	public static IShipSkin getSkin(int skinId){
		if(isAvailable(skinId)){
			switch(skinId){
			case YACHT_SHIP_SKIN:
				return new YachtSkin();
			case EMMA_SHIP_SKIN:
				return new EmmaSkin();
			case BATTLE_SHIP_SKIN:
				return new BattleshipSkin();
			default:
				break;
			}
		}

		return new DefaultShipSkin();
	};

	public static boolean isAvailable(int skinId){
		switch (skinId) {
		case DEFAULT_SHIP_SKIN:
			return true;
		case YACHT_SHIP_SKIN:
			return Docker.getInventory().hasPremium();
		case EMMA_SHIP_SKIN:
			return !Persistence.isLevelLocked("9");
		case BATTLE_SHIP_SKIN:
			return false;
		default:
			return false;
		}
	}

	public static String getSkinName(int skinId){
		switch (skinId) {
		case DEFAULT_SHIP_SKIN:
			return "Default";
		case YACHT_SHIP_SKIN:
			return "Premium Yacht";
		case EMMA_SHIP_SKIN:
			return "Blue Emma";
		case BATTLE_SHIP_SKIN:
			return "Battleship";
		default:
			return "N/A";
		}
	}

	public static int getNextSkin(){
		return getNextSkin(Persistence.getShipSkinId());
	}

	public static int getNextSkin(int skinId){
		int newId = skinId + 1;
		if(newId >= 3)	//Hier anpassen wenns neue Schiffe gibt
			newId = DEFAULT_SHIP_SKIN;
		if(!isAvailable(newId))
			return getNextSkin(newId);
		return newId;
	}
}

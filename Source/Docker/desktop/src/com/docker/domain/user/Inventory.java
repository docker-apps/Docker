package com.docker.domain.user;

import java.util.Date;


public class Inventory implements IInventory{

	@Override
	public boolean hasPremium(){
		// return true as a default
		return true;
	}

	@Override
	public void buyPremium(IInventoryCallback callback){
	}

	@Override
	public void dispose(){		
	}

	@Override
	public void update() {
	}
	
	@Override
	public void update(IInventoryCallback callback){
	}

	@Override
	public Date getLastUpdateDate() {
		return new Date();
	}

	@Override
	public boolean hasBeenUpdated() {
		// return true as a default
		return true;
	}
}

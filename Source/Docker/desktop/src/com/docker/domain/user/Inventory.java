package com.docker.domain.user;

import java.util.Date;


public class Inventory implements IInventory{

	@Override
	public boolean hasPremium(){
		// return true as a default
		return true;
	}

	@Override
	public void buyPremium(){
	}

	@Override
	public void dispose(){		
	}

	@Override
	public void update() {
	}

	@Override
	public Date getLastUpdateDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasBeenUpdated() {
		// TODO Auto-generated method stub
		return false;
	}
}

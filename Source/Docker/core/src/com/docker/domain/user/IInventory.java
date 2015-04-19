package com.docker.domain.user;

import java.util.Date;

public interface IInventory {
	
	public void update();

	public void update(IInventoryCallback callback);
	
	public Date getLastUpdateDate();
	
	public boolean hasBeenUpdated();
	
	public boolean hasPremium();
	
	public void buyPremium(IInventoryCallback callback);
	
	public void dispose();
	
	public interface IInventoryCallback {
		public void call();
	}
}

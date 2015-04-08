package com.docker.domain.user;

import java.util.Date;

public interface IInventory {
	
	public void update();
	
	public Date getLastUpdateDate();
	
	public boolean hasBeenUpdated();
	
	public boolean hasPremium();
	
	public void buyPremium();
	
	public void dispose();
}

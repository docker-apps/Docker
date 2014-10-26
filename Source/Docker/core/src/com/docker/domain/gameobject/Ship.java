package com.docker.domain.gameobject;

import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ship extends Actor {
	private int gridWith;
	private int gridHeight;
	private int carryingCapacity;
	private List<Container> containers;
	
	public Ship(int gridWith, int gridHeight, int carryingCapacity) {
		super();
		this.gridWith = gridWith;
		this.gridHeight = gridHeight;
		this.carryingCapacity = carryingCapacity;
		this.containers = new ArrayList<Container>();
	}
	
	public void addContainer(int gridX, int gridY, Container container){
		throw new NotImplementedException();
	}
	
	public boolean isFree(int gridX, int gridY){
		throw new NotImplementedException();
	}
	
}

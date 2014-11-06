package com.docker.domain.game;

import java.util.List;
import java.util.Stack;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.docker.domain.gameobject.Container;

public class Level {
	private List<Container> containers;
	// better if there is a ship or just the ship measurement?
	private int shipHeight;
	private int shipLength;
	
	public Level(){
		this.containers = new Stack<Container>();
	}
	
	public void load(){
		throw new NotImplementedException();
	}
}

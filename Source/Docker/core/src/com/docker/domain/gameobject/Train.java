package com.docker.domain.gameobject;

import java.util.List;
import java.util.Stack;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Train extends Actor {
	private int speed;
	private List<Container> containers;
	
	public Train(int speed) {
		super();
		this.speed = speed;
		this.containers = new Stack<Container>();
	}
	
	public void pushContainer(Container container){
		throw new NotImplementedException();
	}
	
	public Container popContainer(){
		throw new NotImplementedException();
	}
}

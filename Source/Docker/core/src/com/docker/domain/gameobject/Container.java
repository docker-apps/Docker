package com.docker.domain.gameobject;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Container extends Actor {
	
	public enum ContainerColor {
		RED, GREEN, BLUE, YELLOW
	}
	
	private int weight;
	private int length;
	private ContainerColor color;
	
	public Container(int weight, int length, ContainerColor color) {
		super();
		this.weight = weight;
		this.length = length;
		this.color = color;
	}	
}

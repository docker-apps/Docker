package com.docker.domain.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.docker.domain.gameobject.Container;
import com.docker.domain.gameobject.Ship;
import com.docker.domain.gameobject.Train;

public class Level {
	private List<Integer> containerLengths;
	private List<Integer> containerWeights;
	private int shipHeight = 5;
	private int shipLength = 20;
	private int maxContainerLength = 4;
	private int maxContainerWeight = 5;
	private Train train;
	private Ship ship;
	private int lifeCount;
	
	private Level(){
		this.containerLengths = new ArrayList<Integer>();
		this.containerWeights = new ArrayList<Integer>();
	}
	
	public static Level loadLevel(){
		throw new NotImplementedException();
	}

	private void generateLevel(){
		LinkedList<Container> allContainers = new LinkedList<Container>();
		int line = shipLength;
		int row = shipHeight;
		int containerLength;
		while(row>0){
			while(line>0){
				containerLength = (int)Math.random()*(maxContainerLength+1);
				if(line-containerLength > 2) {
					allContainers.add(new Container(this.generateRandomWeight(), containerLength));
					line -= containerLength;
				}
				else {
					switch (line-containerLength) {
					case 2:
						allContainers.add(new Container(this.generateRandomWeight(), containerLength));
						allContainers.add(new Container(this.generateRandomWeight(), 1));
						allContainers.add(new Container(this.generateRandomWeight(), 1));
						line = 0;				
						break;
					case 1:
						allContainers.add(new Container(this.generateRandomWeight(), containerLength));
						allContainers.add(new Container(this.generateRandomWeight(), 1));
						line = 0;				
						break;
					case 0:
						allContainers.add(new Container(this.generateRandomWeight(), containerLength));
						line = 0;	
						break;
					default:
						allContainers.add(new Container(this.generateRandomWeight(), containerLength+(line-containerLength)));
						line = 0;
						break;
					}
				}
			}
			line = shipHeight;
			row--;
		}
		Collections.shuffle(allContainers);
		train = new Train(allContainers);
		ship = new Ship(shipLength, shipHeight, 5, 0, 0);
	}
	
	private int generateRandomWeight(){
		return (int)Math.random()*(maxContainerWeight+1);
	}
	
	public Ship getShip(){
		return ship;
	}
	public Train getTrain(){
		return train;
	}
	
	public int getLifeCount(){
		return lifeCount;
	}
	
	private int getShipHeight() {
		return shipHeight;
	}

	private int getShipLength() {
		return shipLength;
	}

	private void setShipHeight(int shipHeight) {
		this.shipHeight = shipHeight;
	}

	private void setShipLength(int shipLength) {
		this.shipLength = shipLength;
	}
	
}

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
	private int shipHeight = 5;//values used for random level
	private int shipLength = 20;//values used for random level
	private int maxContainerLength = 4;//random level only
	private final int MAXCONTAINERWEIGHT = 6;//random level only
	private Train train;
	private Ship ship;
	private int lifeCount = 3;//values used for random level
	private int breakThreshold = 10;//values used for random level
	private int capsizeThreshold = 5;//values used for random level
	private int time = 60;//values used for random level
	
	private Level(){
		this.containerLengths = new ArrayList<Integer>();
		this.containerWeights = new ArrayList<Integer>();
	}
	
	public Level createLevel(int index){
		loadLevel(index);
		this.generateSpecifiedLevel();
		return this;
	}
	
	public Level createLevel(){
		this.generateRandomLevel();
		return this;
	}
	
	//persistence method
	private static Level loadLevel(int index){
		throw new NotImplementedException();
	}

	private void generateSpecifiedLevel(){
		//ship does not need carryingCapacity
		this.ship = new Ship(shipLength, shipHeight, breakThreshold, 0, 0);
		LinkedList<Container> allContainers = new LinkedList<Container>();
		while(!containerLengths.isEmpty()){
			allContainers.add(new Container(containerWeights.remove(0), containerLengths.remove(0)));
		}
		this.train = new Train(allContainers);
	}
	
	private void generateRandomLevel(){
		LinkedList<Container> allContainers = new LinkedList<Container>();
		int line = shipLength;
		int row = shipHeight;
		int containerLength;
		while(row>0){
			while(line>0){
				containerLength = (int)Math.random()*(maxContainerLength+1);
				if(line-containerLength > 2) {
					allContainers.add(new Container(this.generateRandomWeight(containerLength), containerLength));
					line -= containerLength;
				}
				else {
					switch (line-containerLength) {
					case 2:
						allContainers.add(new Container(this.generateRandomWeight(containerLength), containerLength));
						allContainers.add(new Container(this.generateRandomWeight(containerLength), 1));
						allContainers.add(new Container(this.generateRandomWeight(containerLength), 1));
						line = 0;				
						break;
					case 1:
						allContainers.add(new Container(this.generateRandomWeight(containerLength), containerLength));
						allContainers.add(new Container(this.generateRandomWeight(containerLength), 1));
						line = 0;				
						break;
					case 0:
						allContainers.add(new Container(this.generateRandomWeight(containerLength), containerLength));
						line = 0;	
						break;
					default:
						allContainers.add(new Container(this.generateRandomWeight(containerLength), containerLength+(line-containerLength)));
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
		ship = new Ship(shipLength, shipHeight, breakThreshold, 0, 0);
	}
	
	private int generateRandomWeight(int containerLength){
		//one-size container have a weight from 1 to 3
		//1-2 80%, 3 20%
		if(containerLength == 1 && (int)(Math.random()*11)<9) {
			return (int)(Math.random()*2)+1;
		} else if(containerLength ==1) {
			return 3;
		}
		//60% of the containers have a weight from 1 to 3
		if((int)(Math.random()*11)<=6){
			return (int)(Math.random()*3)+1;
		} else if ((int)(Math.random()*6)<=4) {
			return (int)(Math.random()*2)+(4);
		}
		return MAXCONTAINERWEIGHT;
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
	
	public int getBreakThreshold(){
		return breakThreshold;
	}
	
	public int getCapsizeThreshold(){
		return capsizeThreshold;
	}
	
	public int getTime(){
		return time;
	}
}

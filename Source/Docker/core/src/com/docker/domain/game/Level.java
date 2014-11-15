package com.docker.domain.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.JsonValue;
import com.docker.technicalservices.Persistence;

import com.docker.domain.gameobject.Container;
import com.docker.domain.gameobject.Ship;
import com.docker.domain.gameobject.Train;

public class Level {
	private List<Integer> containerLengths;
	private List<Integer> containerWeights;
	private int shipHeight;
	private int shipLength;
	private final int MAXCONTAINERLENGTH = 4;
	private final int MAXCONTAINERWEIGHT = 6;
	private Train train;
	private Ship ship;
	private int lifeCount;
	private int breakThreshold;
	private int capsizeThreshold;
	private int time;
	
	public Level(List<Integer> containerLengths,
			List<Integer> containerWeights, int shipHeight, int shipLength,
			int lifeCount, int breakThreshold, int capsizeThreshold, int time) {
		super();
		this.containerLengths = containerLengths;
		this.containerWeights = containerWeights;
		this.shipHeight = shipHeight;
		this.shipLength = shipLength;
		this.lifeCount = lifeCount;
		this.breakThreshold = breakThreshold;
		this.capsizeThreshold = capsizeThreshold;
		this.time = time;
	}

    public static Level loadLevel(String id, Persistence persistence){
        Level level = parseLevel(persistence.getLevel(id));
        if (level != null) {
            level.generateSpecifiedLevel();
            return level;
        }
        return null;
	}
	
	public static Level loadLevel(){
		Level level = new Level(new LinkedList<Integer>(), new LinkedList<Integer>(), 5, 20, 3, 10, 5, 60);
		level.generateRandomLevel();
		return level;
	}
	
	//persistence method

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
				containerLength = ((int)Math.random()*(MAXCONTAINERLENGTH))+1;
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

    private static Level parseLevel(JsonValue level) {
        if (level!= null) {
            List<Integer> containerLengths = new ArrayList<Integer>();
            List<Integer> containerWeights = new ArrayList<Integer>();
            JsonValue containerList = level.get("containers").get("container");
            int trainSpeed = level.get("trainSpeed").asInt();
            for (JsonValue container : containerList) {
                containerLengths.add(container.getInt("length"));
                containerWeights.add(container.getInt("weight"));
            }
            return new Level(containerLengths, containerWeights, level.get("shipHeight").asInt(),
                    level.getInt("shipLength"), level.getInt("lives"), level.getInt("breakThreshold"),
                    level.getInt("capsizeThreshold"), 0);
        }
        return null;
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

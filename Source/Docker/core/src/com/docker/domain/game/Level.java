package com.docker.domain.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.JsonValue;
import com.docker.Docker;
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
    private int trainSpeed;
	
	public Level(List<Integer> containerLengths,
			List<Integer> containerWeights, int shipHeight, int shipLength,
			int lifeCount, int breakThreshold, int capsizeThreshold, int time, int trainSpeed) {
		super();
		this.containerLengths = containerLengths;
		this.containerWeights = containerWeights;
		this.shipHeight = shipHeight;
		this.shipLength = shipLength;
		this.lifeCount = lifeCount;
		this.breakThreshold = breakThreshold;
		this.capsizeThreshold = capsizeThreshold;
        this.time = time;
        this.trainSpeed = trainSpeed;
	}

    public static Level loadLevel(String id){
        Level level = parseLevel(Persistence.getLevel(id));
        if (level != null) {
            level.generateSpecifiedLevel();
            return level;
        }
        return null;
	}
	
	public static Level loadLevel(){
		Level level = new Level(new LinkedList<Integer>(), new LinkedList<Integer>(), 5, 10, 3, 10, 20, 60, 15);
		level.generateRandomLevel();
		return level;
	}
	
	//persistence method

	private void generateSpecifiedLevel(){
		//ship does not need carryingCapacity
		this.ship = new Ship(shipLength, shipHeight, capsizeThreshold, breakThreshold, 0, 0);
		this.initShipPosition();
		LinkedList<Container> allContainers = new LinkedList<Container>();
		while(!containerLengths.isEmpty()){
			allContainers.add(new Container(containerWeights.remove(0), containerLengths.remove(0)));
		}
		this.train = new Train(allContainers, trainSpeed);
		this.initTrainPosition();
	}
	
	private void generateRandomLevel(){
		LinkedList<Container> allContainers = new LinkedList<Container>();
		int line = shipLength;
		int row = shipHeight;
		int containerLength;
		while(row>0){
			while(line>0){
				containerLength = (int)(Math.random()*(MAXCONTAINERLENGTH))+1;
				if(line-containerLength > 2) {
					allContainers.add(new Container(this.generateRandomWeight(containerLength), containerLength));
					line -= containerLength;
				}
				else {
					switch (line-containerLength) {
					case 2:
						allContainers.add(new Container(this.generateRandomWeight(containerLength), containerLength));
						allContainers.add(new Container(this.generateRandomWeight(1), 1));
						allContainers.add(new Container(this.generateRandomWeight(1), 1));
						line = 0;
						break;
					case 1:
						allContainers.add(new Container(this.generateRandomWeight(containerLength), containerLength));
						allContainers.add(new Container(this.generateRandomWeight(1), 1));
						line = 0;				
						break;
					case 0:
						allContainers.add(new Container(this.generateRandomWeight(containerLength), containerLength));
						line = 0;	
						break;
					default:
						containerLength = line;
						allContainers.add(new Container(this.generateRandomWeight(containerLength), containerLength));
						line = 0;
						break;
					}
				}
			}
			line = shipLength;
			row--;
		}
		Collections.shuffle(allContainers);
		
		this.train = new Train(allContainers, trainSpeed);
		this.initTrainPosition();
		this.ship = new Ship(shipLength, shipHeight, capsizeThreshold, breakThreshold, 0f, 10f);
		this.initShipPosition();
	}
	
	private int generateRandomWeight(int containerLength){
		//one-size container have a weight from 1 to 3
		//1-2 80%, 3 20%
		if(containerLength == 1 && (int)(Math.random()*10)<8) {
			return (int)(Math.random()*2)+1;
		} else if(containerLength ==1) {
			return 3;
		}
		//60% of the containers have a weight from 1 to 3
		if((int)(Math.random()*10)<6){
			return (int)(Math.random()*3)+1;
		} else if ((int)(Math.random()*8)<7) {
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
                    level.getInt("capsizeThreshold"), 0, trainSpeed);
        }
        return null;
    }
    
    private void initShipPosition(){
    	this.ship.setPosition(
    			(Docker.WIDTH-ship.getWidth())/2-20f,
    			10f);
    }
    
    private void initTrainPosition(){
    	this.train.setPosition(0f, Docker.HEIGHT-23);
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

    public int getTrainSpeed() {
        return trainSpeed;
    }
}

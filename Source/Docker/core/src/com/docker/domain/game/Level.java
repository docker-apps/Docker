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

/**
 * Level class which loads levels from persistence or generates random level with specified input
 * @author it-monkey
 *
 */
public class Level {
	private List<Integer> containerLengths;
	private List<Integer> containerWeights;
	private int shipHeight;
	private int shipLength;
	private final static int MAXCONTAINERLENGTH = 4;
	private final static int MAXCONTAINERWEIGHT = 6;
	private Train train;
	private Ship ship;
	private int lifeCount;
	private int breakThreshold;
	private int capsizeThreshold;
	private int time;
    private int trainSpeed;
	
	private Level(List<Integer> containerLengths,
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

	/**
	 * Loads the level from persistence with the given id
	 * @param id
	 * @return level
	 */
    public static Level loadLevel(String id){
        Level level = parseLevel(Persistence.getLevel(id));
        if (level != null) {
            level.generateSpecifiedLevel();
            return level;
        }
        return null;
	}
	
    /**
     * Loads a random level with the hard coded parameters.
     * @return level
     */
	public static Level loadLevel(){
		Level level = new Level(new LinkedList<Integer>(), new LinkedList<Integer>(), 5, 10, 3, 5, 20, 60, 15);
		level.generateRandomLevel();
		return level;
	}
	
	/**
	 * Generates a predefined level with the given class variables. 
	 */
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
	
	/**
	 * Generates a random level with the given class variables.
	 */
	private void generateRandomLevel(){
		LinkedList<Container> allContainers = new LinkedList<Container>();
		int line = shipLength;
		int row = shipHeight;
		int containerLength;
		while(row>0){
			while(line>0){
				containerLength = (int)(Math.random()*(MAXCONTAINERLENGTH))+1;
				if(line-containerLength > 2) {
					allContainers.add(new Container(generateRandomWeight(containerLength), containerLength));
					line -= containerLength;
				}
				else {
					switch (line-containerLength) {
					case 2:
						allContainers.add(new Container(generateRandomWeight(containerLength), containerLength));
						allContainers.add(new Container(generateRandomWeight(1), 1));
						allContainers.add(new Container(generateRandomWeight(1), 1));
						line = 0;
						break;
					case 1:
						allContainers.add(new Container(generateRandomWeight(containerLength), containerLength));
						allContainers.add(new Container(generateRandomWeight(1), 1));
						line = 0;				
						break;
					case 0:
						allContainers.add(new Container(generateRandomWeight(containerLength), containerLength));
						line = 0;	
						break;
					default:
						containerLength = line;
						allContainers.add(new Container(generateRandomWeight(containerLength), containerLength));
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
	
	/**
	 * Generates a random weight for the container, depending on the length of it.
	 * @param containerLength
	 * @return randomWeight (1-3 if container length is 1, or 1-6 if container length is between 2 and 4)
	 */
	private static int generateRandomWeight(int containerLength){
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

	/**
	 * Parses level from persistence and returns a new level.
	 * @param level
	 * @return level
	 */
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
    
    /**
     * 
     * @return a random created Container
     */
    public static Container createRandomContainer(){
		int containerLength = (int)(Math.random()*(MAXCONTAINERLENGTH))+1;
    	Container container = new Container(generateRandomWeight(containerLength), containerLength);
    	return container;
    }
    
    /**
     * Initializes the position of the ship.
     */
    private void initShipPosition(){
    	this.ship.setPosition(
    			(Docker.WIDTH-ship.getWidth())/2-20f,
    			10f);
    }
    
    /**
     * Initializes the position of the train.
     */
    private void initTrainPosition(){
    	this.train.setPosition(0f, Docker.HEIGHT-23);
    }

    /**
     * simple getter
     * @return ship
     */
	public Ship getShip(){
		return ship;
	}
	
	/**
	 * simple getter
	 * @return train
	 */
	public Train getTrain(){
		return train;
	}
	
	/**
	 * simple getter
	 * @return lifeCount
	 */
	public int getLifeCount(){
		return lifeCount;
	}	
	
	/**
	 * simple getter
	 * @return breakThreshold
	 */
	public int getBreakThreshold(){
		return breakThreshold;
	}
	
	/**
	 * simple getter
	 * @return capsizeThreshold
	 */
	public int getCapsizeThreshold(){
		return capsizeThreshold;
	}
	
	/**
	 * simple getter
	 * @return time
	 */
	public int getTime(){
		return time;
	}

	/**
	 * simple getter
	 * @return trainSpeed
	 */
    public int getTrainSpeed() {
        return trainSpeed;
    }
}

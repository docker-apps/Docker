package com.docker.domain.user;

import com.badlogic.gdx.utils.ObjectMap;
import com.docker.technicalservices.Persistence;

public class Statistics {
	private int totalContainer;
	private int totalWeight;
	private int totalGames;
	private int totalShipsSuccessfullyLoaded;
	private int totalShipsCapsized;
	private int totalShipsBroken;
	
	public Statistics() {
		this.totalContainer = 0;
		this.totalWeight = 0;
		this.totalGames = 0;
		this.totalShipsSuccessfullyLoaded = 0;
		this.totalShipsCapsized = 0;
		this.totalShipsBroken = 0;
	}

    public Statistics(int totalContainer, int totalWeight, int totalGames,
                      int totalShipsSuccessfullyLoaded, int totalShipsCapsized, int totalShipsBroken) {
        this.totalContainer = totalContainer;
        this.totalWeight = totalWeight;
        this.totalGames = totalGames;
        this.totalShipsSuccessfullyLoaded = totalShipsSuccessfullyLoaded;
        this.totalShipsCapsized = totalShipsCapsized;
        this.totalShipsBroken = totalShipsBroken;
    }

	public int getTotalContainer() {
		return totalContainer;
	}
	public int getTotalWeight() {
		return totalWeight;
	}
	public int getTotalGames() {
		return totalGames;
	}
	public int getTotalShipsSuccessfullyLoaded() {
		return totalShipsSuccessfullyLoaded;
	}
	public int getTotalShipsCapsized() {
		return totalShipsCapsized;
	}
	public int getTotalShipsBroken() {
		return totalShipsBroken;
	}
	public void incrementTotalContainer(int totalContainer) {
		this.totalContainer += totalContainer;
	}
	public void incrementTotalWeight(int totalWeight) {
		this.totalWeight += totalWeight;
	}
	public void incrementTotalGames(int totalGames) {
		this.totalGames += totalGames;
	}
	public void incrementTotalShipsSuccessfullyLoaded(int totalShipsSuccessfullyLoaded) {
		this.totalShipsSuccessfullyLoaded += totalShipsSuccessfullyLoaded;
	}
	public void incrementTotalShipsCapsized(int totalShipsCapsized) {
		this.totalShipsCapsized += totalShipsCapsized;
	}
	public void incrementTotalShipsBroken(int totalShipsBroken) {
		this.totalShipsBroken += totalShipsBroken;
	}

    public void persistTotalContainer(int totalContainer) {
        Persistence.saveStatisticValue("totalContainer", totalContainer);
    }
    public void persistTotalWeight(int totalWeight) {
        Persistence.saveStatisticValue("totalWeight", totalWeight);
    }
    public void persistTotalGames(int totalGames) {
        Persistence.saveStatisticValue("totalGames", totalGames);
    }
    public void persistTotalShipsSuccessfullyLoaded(int totalShipsSuccessfullyLoaded) {
        Persistence.saveStatisticValue("totalShipsSuccessfullyLoaded", totalShipsSuccessfullyLoaded);
    }
    public void persistTotalShipsCapsized(int totalShipsCapsized) {
        Persistence.saveStatisticValue("totalShipsCapsized", totalShipsCapsized);
    }
    public void persistTotalShipsBroken(int totalShipsBroken) {
        Persistence.saveStatisticValue("totalShipsBroken", totalShipsBroken);
    }

    public void persistStatistics(Statistics statistics) {
        ObjectMap<String, Object> map = new ObjectMap<String, Object>();
        map.put("totalContainer", statistics.getTotalContainer());
        map.put("totalWeight", statistics.getTotalWeight());
        map.put("totalGames", statistics.getTotalGames());
        map.put("totalShipsSuccessfullyLoaded", statistics.getTotalShipsSuccessfullyLoaded());
        map.put("totalShipsCapsized", statistics.getTotalShipsCapsized());
        map.put("totalShipsBroken", statistics.getTotalShipsBroken());
        Persistence.writeStatisticMap(map);
    }

}

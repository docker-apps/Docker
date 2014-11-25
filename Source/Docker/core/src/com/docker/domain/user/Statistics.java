package com.docker.domain.user;

import com.badlogic.gdx.utils.ObjectMap;
import com.docker.technicalservices.Persistence;

public class Statistics {
	private int totalScore;
	private int totalContainer;
	private int totalWeight;
	private int totalGames;
	private int totalShipsSuccessfullyLoaded;
	private int totalShipsCapsized;
	private int totalShipsBroken;
	
	public Statistics() {
		this.totalScore = 0;
		this.totalContainer = 0;
		this.totalWeight = 0;
		this.totalGames = 0;
		this.totalShipsSuccessfullyLoaded = 0;
		this.totalShipsCapsized = 0;
		this.totalShipsBroken = 0;
	}

    public Statistics(int totalScore, int totalContainer, int totalWeight, int totalGames,
                      int totalShipsSuccessfullyLoaded, int totalShipsCapsized, int totalShipsBroken) {
        this.totalScore = totalScore;
        this.totalContainer = totalContainer;
        this.totalWeight = totalWeight;
        this.totalGames = totalGames;
        this.totalShipsSuccessfullyLoaded = totalShipsSuccessfullyLoaded;
        this.totalShipsCapsized = totalShipsCapsized;
        this.totalShipsBroken = totalShipsBroken;
    }

    public int getTotalScore() {
		return totalScore;
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
	public void incrementTotalScore(int totalScore) {
		this.totalScore += totalScore;
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

    public void persistTotalScore(int totalScore, Persistence persistence) {
        Persistence.saveStatisticValue("totalScore", totalScore);
    }
    public void persistTotalContainer(int totalContainer, Persistence persistence) {
        Persistence.saveStatisticValue("totalContainer", totalContainer);
    }
    public void persistTotalWeight(int totalWeight, Persistence persistence) {
        Persistence.saveStatisticValue("totalWeight", totalWeight);
    }
    public void persistTotalGames(int totalGames, Persistence persistence) {
        Persistence.saveStatisticValue("totalGames", totalGames);
    }
    public void persistTotalShipsSuccessfullyLoaded(int totalShipsSuccessfullyLoaded, Persistence persistence) {
        Persistence.saveStatisticValue("totalShipsSuccessfullyLoaded", totalShipsSuccessfullyLoaded);
    }
    public void persistTotalShipsCapsized(int totalShipsCapsized, Persistence persistence) {
        Persistence.saveStatisticValue("totalShipsCapsized", totalShipsCapsized);
    }
    public void persistTotalShipsBroken(int totalShipsBroken, Persistence persistence) {
        Persistence.saveStatisticValue("totalShipsBroken", totalShipsBroken);
    }

    public void persistStatistics(Persistence persistence, Statistics statistics) {
        ObjectMap<String, Object> map = new ObjectMap<String, Object>();
        map.put("totalScore", statistics.getTotalScore());
        map.put("totalContainer", statistics.getTotalContainer());
        map.put("totalWeight", statistics.getTotalWeight());
        map.put("totalGames", statistics.getTotalGames());
        map.put("totalShipsSuccessfullyLoaded", statistics.getTotalShipsSuccessfullyLoaded());
        map.put("totalShipsCapsized", statistics.getTotalShipsCapsized());
        map.put("totalShipsBroken", statistics.getTotalShipsBroken());
        Persistence.writeStatisticMap(map);
    }

}

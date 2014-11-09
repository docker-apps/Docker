package com.docker.domain.user;

public class Statistics {
	private int totalScore;
	private int totalContainer;
	private int totalWeight;
	private int totalGames;
	private int totalShipsSuccessfullyLoaded;
	private int totalShipsCapsized;
	private int totalShipsBroken;
	
	//wie wird die Statistik persistent abgelegt und wieder geladen?
	public Statistics(){
		this.totalScore = 0;
		this.totalContainer = 0;
		this.totalWeight = 0;
		this.totalGames = 0;
		this.totalShipsSuccessfullyLoaded = 0;
		this.totalShipsCapsized = 0;
		this.totalShipsBroken = 0;
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
}

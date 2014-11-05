package com.docker.domain.game;

import sun.security.x509.DeltaCRLIndicatorExtension;

public class LoadRating {

	private float[][] loadTable;
	private float breakThreshold;
	private float capsizeThreshold;
	private float[] breakValues;
	private Float breakValueSum;
	private float capsizeValue;
	private float handicapFactor;
	private int score;
	public enum Capsized {LEFT, RIGHT, NONE};
	private float[] loadDeltas;
	private float[] loadSums;
	
	public LoadRating(float breakValue, float capsizeValue, float handicapFactor){
		this.breakThreshold = breakValue;
		this.capsizeThreshold = capsizeValue;
		this.handicapFactor = handicapFactor;
		
	}

	public void calculate(float[][] loadTable) throws Exception{
		this.setLoadTable(loadTable);
		this.calculateLoadDelta();
		this.calculateCapsized();
		this.calculateBreak();
	}
	
	public void calculateScore(float[][] loadTable) throws Exception {
		this.calculate(loadTable);
		score = (int)Math.round(1000*(breakThreshold-breakValueSum/loadSums.length)/breakThreshold);
		score += (int)Math.round(1000*(capsizeThreshold-Math.abs(capsizeValue))/capsizeThreshold);
		//score += score beauty
	}
	
	/*
	 * returns a value which could be negative or positive, |values| larger than 1 mean
	 * the ship will capsize. If the Values are positive there is more weight on the
	 * leftern half of the ship, if it is negative there is more weight on the 
	 * rightern half.
	 */
	private void calculateCapsized() {
		capsizeValue = 0;
		int middle = loadSums.length/2;
		for (int i = 0; i < middle; i++) {
			capsizeValue += loadSums[i]*(float)(middle-i);
			
		}
		for (int i = loadSums.length; i > loadSums.length-1-middle; i--){
			capsizeValue -= loadSums[i-1]*(float)(i/2);
		}
		capsizeValue = capsizeValue/capsizeThreshold;
		
	}
	
	/*
	 * returns an array of values which are positive, values larger than 1 mean
	 * the ship will burst.
	 */
	private void calculateBreak() {
		breakValues = new float[loadTable.length];
		breakValueSum = 0f;
		
		for (int i = 0; i < breakValues.length; i++) {
			if(i==0){
				breakValues[i] = loadDeltas[i]/breakThreshold;
			} if(i==loadDeltas.length){
				breakValues[i] = loadDeltas[i-1]/breakThreshold;
			} else {
				if(loadDeltas[i-1]>loadDeltas[i]){
					breakValues[i] = loadDeltas[i-1]/breakThreshold;
				} else {
					breakValues[i] = loadDeltas[i]/breakThreshold;
				}
			}
			breakValueSum += breakValues[i];
		}
	}
	
	private void calculateBeauty(){
		//calculate beauty of the load
	}

	private void calculateLoadDelta() throws Exception{
		if(loadTable == null)
			throw new Exception("loadTable not initialized");
		
		loadSums = new float[loadTable.length];
		loadDeltas = new float[loadTable.length-1];
		
		for (int i = 0; i < loadTable.length; i++) {
			loadSums[i] = 0;
			for (int j = 0; j < loadTable[i].length; j++) {
				loadSums[i]+=loadTable[i][j];
			}
		}
		
		for (int i = 0; i < loadDeltas.length; i++) {
			loadDeltas[i] = Math.abs(loadSums[i]-loadSums[i+1]);
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public float getCapsizeValue() {
		return capsizeValue;
	}
	
	public float[] getBreakValues(){
		return breakValues;
	}

	private void setLoadTable(float[][] loadTable) {
		this.loadTable = loadTable;
	}
	
}

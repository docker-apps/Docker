package com.docker.domain.game;


public class LoadRating {

	private float[][] loadTable;
	private float breakThreshold; //test with 5
	private float capsizeThreshold; //test with 5
	private float[] breakValues;
	private float breakValueSum;
	private float capsizeValue;
	private float handicapFactor;
	private int score;
	public enum Capsized {LEFT, RIGHT, NONE};
	private float[] loadDeltas;
	private float[] loadSums;
	private int beauty;
	
	public LoadRating(float breakValue, float capsizeValue, float handicapFactor){
		this.breakThreshold = breakValue;
		this.capsizeThreshold = capsizeValue;
		this.handicapFactor = handicapFactor;
		
	}

	public void calculate(float[][] loadTable){
		this.setLoadTable(loadTable);
		this.calculateLoadDelta();
		this.calculateCapsized();
		this.calculateBreak();
		this.calculateBeauty();
	}
	
	public void calculateScore(float[][] loadTable){
		this.calculate(loadTable);
		float tempscore;
		float tempBreak;
		float tempCapsize;
		float tempBeauty;
		float factor;
		System.out.println(breakValueSum+" "+breakThreshold+" "+capsizeThreshold+" "+capsizeValue);
		factor = 1;//(breakThreshold-breakValueSum)/breakThreshold;
		//tempBreak = 300*(breakThreshold-breakValueSum)/breakThreshold;
		if(1 > Math.abs(capsizeValue)){
			factor = factor*((1-Math.abs(capsizeValue*3/6))/1);
			tempCapsize = 450*((1-Math.abs(capsizeValue*3/6))/1);
		}else{
			factor = factor*0;
			tempCapsize = 0;
		}
		factor = factor*(float)beauty/10;
		tempBeauty = 45*beauty;
		tempscore = /*tempBreak +*/ tempCapsize + tempBeauty + factor*2100;
		score = (int)Math.round(tempscore*handicapFactor);
	}
	
	/*
	 * returns a value which could be negative or positive, |values| larger than 1 mean
	 * the ship will capsize. If the Values are positive there is more weight on the
	 * leftern half of the ship, if it is negative there is more weight on the 
	 * rightern half.
	 */
	private void calculateCapsized() {
		capsizeValue = 0;
		int middle = (int) Math.ceil(loadSums.length/2);
		for (int i = 0; i < middle; i++) {
			capsizeValue += loadSums[i]*(float)(middle-i);
			
		}
		int factor = middle;
		for (int i = loadSums.length; i > loadSums.length-middle; i--){
			capsizeValue -= loadSums[i-1]*(float)(factor);
			factor--;
		}
		capsizeValue = capsizeValue/capsizeThreshold;
		
	}
	
	/*
	 * returns an array of values which are positive, values larger than 1 mean
	 * the ship will burst.
	 */
	private void calculateBreak() {
		breakValues = new float[loadTable.length];
		breakValueSum = 0;
		
		for (int i = 0; i < breakValues.length; i++) {
			if(i==0){
				breakValues[i] = loadDeltas[i]/breakThreshold;
			} else if(i==loadDeltas.length){
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
		beauty = 10;
		int[] holes = new int[loadTable[0].length];
		
		for (int i = loadTable[0].length-1; i >= 0; i--) {
			for (int j = 0; j < loadTable.length; j++) {
				if(loadTable[j][i] == 0)
					holes[loadTable[0].length-1-i]++;
			}
		}
		
		boolean a = false;
		int count = 1;
		for (int i = 0; i < holes.length; i++) {
			if(a){
				beauty -= holes[i]*count;
				count++;
			}
			if(holes[i] != loadTable.length && a == false)
				a = true;
			
			if(beauty < 0)
				beauty = 0;
		}
	}

	private void calculateLoadDelta(){
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

	public float[] getLoadSums() {
		return loadSums;
	}

	public void setLoadSums(float[] loadSums) {
		this.loadSums = loadSums;
	}
	
}

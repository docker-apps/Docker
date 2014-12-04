package com.docker.domain.game;

/**
 * Load rating which calculates the load balance and score of the game
 * @author it-monkey
 *
 */
public class LoadRating {

	private float[][] loadTable;
	private float breakThreshold; //test with 5
	private float capsizeThreshold; //test with 5
	private float[] breakValues;
	private float capsizeValue;
	private float handicapFactor;
	private int score;
	private float[] loadDeltas;
	private float[] loadSums;
	private int beauty;
	
	/**
	 * Constructor for the Loadrating class
	 * @param breakValue Value when the ship will break 0 might be to low, use 5 or 6
	 * @param capsizeValue Value when the ship will capsize 0 might be to low, use 7 or 8
	 * @param handicapFactor Value which multiplies the score with a specific factor
	 */
	public LoadRating(float breakValue, float capsizeValue, float handicapFactor){
		this.breakThreshold = breakValue;
		this.capsizeThreshold = capsizeValue;
		this.handicapFactor = handicapFactor;
		
	}

	/**
	 * Calculates the load rating from a given loadTable and writes the calculated values into the breakValue, capsizeValue, and beautyValue
	 * @param loadTable two dimensional array of the weight on each position of the ship
	 */
	public void calculate(float[][] loadTable){
		this.setLoadTable(loadTable);
		this.calculateLoadDelta();
		this.calculateCapsized();
		this.calculateBreak();
		this.calculateBeauty();
	}
	
	/**
	 * Calculates the score from a given loadTable, includes the calculate() method
	 * @param loadTable two dimensional array of the weight on each position of the ship
	 */
	public void calculateScore(float[][] loadTable){
		this.calculate(loadTable);
		float tempscore;
		float tempCapsize;
		float tempBeauty;
		float factor;
		
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
	
	/**
	 * writes a value into capsizeValue which could be negative or positive, |values| larger than 1 mean
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
	
	/**
	 * writes an array of values into breakValues which are positive, values larger than 1 mean
	 * the ship will burst.
	 */
	private void calculateBreak() {
		breakValues = new float[loadTable.length];
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
		}
	}
	
	/**
	 * writes an integer into beautyValue which reaches from 10 to 0. 10 is the highest value and 0 the lowest.
	 */
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

	/**
	 * calculates the loadDelta between the columns of the loadTable. The delta values will be written into the loadDeltas array.
	 */
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
	
	/**
	 * returns the score
	 * @return score
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * returns the capsizeValue
	 * @return capsizeValue
	 */
	public float getCapsizeValue() {
		return capsizeValue;
	}
	
	/**
	 * returns the breakValues
	 * @return breakValues
	 */
	public float[] getBreakValues(){
		return breakValues;
	}
	
	/**
	 * returns -1 if the Ship doesn't break
	 * @return burstposition
	 */
	public int doesBreak(){
		int burstpos = -1;
		for (int i = 0; i < breakValues.length; i++) {
			if(breakValues[i]>=1){
				burstpos = i;
			}
		}
		return burstpos;
	}

	/**
	 * sets the new loadTable
	 * @param loadTable
	 */
	private void setLoadTable(float[][] loadTable) {
		this.loadTable = loadTable;
	}

	/**
	 * returns an array of the loadSums;
	 * @return loadSums
	 */
	public float[] getLoadSums() {
		return loadSums;
	}

	/**
	 * sets an array of the loadSums;
	 * @param loadSums
	 */
	public void setLoadSums(float[] loadSums) {
		this.loadSums = loadSums;
	}
	
}

package com.docker.domain.game;

public class LoadRating {

	private float[][] loadTable;
	private float breakValue;
	private float capsizeValue;
	private float handicapFactor;
	private int score;
	
	public LoadRating(float breakValue, float capsizeValue, float handicapFactor){
		this.breakValue = breakValue;
		this.capsizeValue = capsizeValue;
		this.handicapFactor = handicapFactor;
	}

	//wie werden die Werte angegeben, um anzuzeigen wie einseitig die Belastung ist?
	//allenfalls mit Enum left right none?
	public boolean isCapsized() {
		
	}
	
	//wie werden die Werte angegeben, um anzuzeigen, das die Bruchbelastung fast erreicht ist?
	//müsste ein boolean array (länge = schiffslänge) 
	//ausgeben in dem die Position angezeigt wird.
	public boolean isBroken() {
		
	}
	
	//Berechnet Punkte ohne oder mit überprüfung ob das Schiff kippt?
	public int getScore() {
		return score;
	}

	public void setLoadTable(float[][] loadTable) {
		this.loadTable = loadTable;
	}
	
}

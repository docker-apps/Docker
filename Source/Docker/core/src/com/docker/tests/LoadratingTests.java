package com.docker.tests;


import static org.junit.Assert.*;

import org.junit.Test;

import com.docker.domain.game.LoadRating;

public class LoadratingTests {

	@Test
	public void testCalculateScore(){
		float[][] loadTable = {{1,1},{1,1}};
		LoadRating loadRating = new LoadRating(3, 3, 1);
		loadRating.calculateScore(loadTable);
		int score = loadRating.getScore();
		assertEquals(200, score);
	}

	@Test
	public void testCalculateCapsizeValueLeft() {
		float[][] loadTable = {{5,1,1},{0,0,0},{0,0,0}};
		LoadRating loadRating = new LoadRating(3, 3, 1);
		loadRating.calculateScore(loadTable);
		float capsize = loadRating.getCapsizeValue();
		assertTrue("Capsize is not calculated correctly", 1 <= capsize);
	}
	
	@Test
	public void testCalculateCapsizeValueRight() {
		float[][] loadTable = {{0,0,0},{0,0,0},{5,1,1}};
		LoadRating loadRating = new LoadRating(3, 3, 1);
		loadRating.calculateScore(loadTable);
		float capsize = loadRating.getCapsizeValue();
		assertTrue("Capsize is not calculated correctly", -1 >= capsize);
	}
	
	@Test
	public void testNoCapsize() {
		float[][] loadTable = {{5,1,1},{5,2,1},{5,4,1}};
		LoadRating loadRating = new LoadRating(3, 3, 1);
		loadRating.calculateScore(loadTable);
		float capsize = loadRating.getCapsizeValue();
		assertEquals("Capsize is not calculated correctly", 0, capsize , 1);
	}

	@Test
	public void testGetBreakValues() {
		float[][] loadTable = {{0,0,0},{0,0,0},{5,1,1}};
		LoadRating loadRating = new LoadRating(3, 3, 1);
		loadRating.calculateScore(loadTable);
		float[] breakvalues = loadRating.getBreakValues();
		assertEquals("Breakvalues are not calculated correctly", 2.33, breakvalues[1], 0.1);
	}

	@Test
	public void testDoesBreak() {
		float[][] loadTable = {{0,0,0},{0,0,0},{5,1,1}};
		LoadRating loadRating = new LoadRating(3, 3, 1);
		loadRating.calculateScore(loadTable);
		assertTrue("BurstValue is wrong", -1 != loadRating.doesBreak());
	}
	
	@Test
	public void testNotDoesBreak() {
		float[][] loadTable = {{5,1,1},{5,1,1},{5,1,1}};
		LoadRating loadRating = new LoadRating(3, 3, 1);
		loadRating.calculateScore(loadTable);
		assertTrue("BurstValue is wrong", -1 == loadRating.doesBreak());
	}
	
	



}

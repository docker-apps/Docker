package com.docker.ui.menus;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.docker.Docker;
import com.docker.technicalservices.Persistence;

public class StatisticsMenu extends AbstractMenu {
    private TextButton backButton = new TextButton("Back", skin);
    private Label title = new Label("Statistics",skin, "title");

    private static Map<String,String> labelMap = new HashMap<String, String>();

    public StatisticsMenu(final Docker application) {
    	super(application);
    	
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.returnToLastScreen();
            }
        });
        setLabelMap();
        
        Table statisticsTable = new Table();
        statisticsTable.add(title).fillX().padBottom(10).colspan(2).row();
        loadStatistics(statisticsTable);
        statisticsTable.add(backButton).bottom().padBottom(5).row();
        ScrollPane scrollpane = new StatisticsScrollPane(statisticsTable);
        scrollpane.setPosition(0, 0);
        scrollpane.setSize(Docker.WIDTH, Docker.HEIGHT);
        scrollpane.setFlingTime(2);
        scrollpane.setupOverscroll(20, 30, 200);
        scrollpane.setFadeScrollBars(false);
        this.table.addActor(scrollpane);
    }

    private void loadStatistics(Table table) {
        ObjectMap<String, Object> statisticsMap = Persistence.getStatisticsMap();
        for (ObjectMap.Entry<String, Object> stringObjectEntry : statisticsMap) {
            Label l = new Label(labelMap.get(stringObjectEntry.key), skin);
            Label v = new Label(stringObjectEntry.value.toString(), skin);
            table.add(l).width(300).left().padBottom(5);
            table.add(v).row().padBottom(5);
        }
    }

    public void setLabelMap() {
        labelMap.put("totalQuickScore", "Quick Game Highscore");
        labelMap.put("totalInfiniteScore", "Infinite Game Highscore");
        labelMap.put("totalContainer", "Total Containers Loaded");
        labelMap.put("totalWeight", "Total Weight");
        labelMap.put("totalGames", "Total Games Played");
        labelMap.put("totalShipsSuccessfullyLoaded", "Total Ships Loaded");
        labelMap.put("totalShipsCapsized", "Total Ships Capsized");
        labelMap.put("totalShipsBroken", "Total Ships Broken");
    }
    
    private class StatisticsScrollPane extends ScrollPane{
    	
    	ShapeRenderer shapeRenderer = new ShapeRenderer();
    	
    	public StatisticsScrollPane(Actor actor){
    		super(actor);
    	}
    	
    	@Override
        public void draw(Batch batch, float parentAlpha){
        	batch.end();
    		Gdx.gl.glEnable(GL30.GL_BLEND);
    	    Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
    		shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
    		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
    		shapeRenderer.begin(ShapeType.Filled);
    		shapeRenderer.setColor(0f, 0f, 0f, 0.4f);
    		shapeRenderer.rect(0f, 0f, this.getStage().getWidth(), this.getStage().getHeight());
    		shapeRenderer.end();
    		batch.begin();
    		
    		super.draw(batch, parentAlpha);
        }
    }
}

package com.docker.ui.menus;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.docker.Docker;
import com.docker.technicalservices.Persistence;

public class StatisticsMenu extends AbstractMenu {
    private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    private TextButton backButton = new TextButton("back", skin);
    private Label title = new Label("Statistics",skin);

    private static Map<String,String> labelMap = new HashMap<String, String>();

    public StatisticsMenu(final Docker application) {
    	super(application);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setLastScreen();
            }
        });
        setLabelMap();
        
        Table statisticsTable = new Table();
        statisticsTable.add(title).padBottom(10).row();
        loadStatistics(statisticsTable);
        statisticsTable.add(backButton).bottom().padBottom(5).row();
        ScrollPane scrollpane = new ScrollPane(statisticsTable);
        scrollpane.setPosition(0, 0);
        scrollpane.setSize(Docker.WIDTH, Docker.HEIGHT);
        scrollpane.setFlingTime(2);
        scrollpane.setupOverscroll(20, 30, 200);
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
        labelMap.put("totalScore", "total highscore");
        labelMap.put("totalContainer", "total containers loaded");
        labelMap.put("totalWeight", "total weight");
        labelMap.put("totalGames", "total games played");
        labelMap.put("totalShipsSuccessfullyLoaded", "total ships successfully loaded");
        labelMap.put("totalShipsCapsized", "total ships capsized");
        labelMap.put("totalShipsBroken", "total ships broken");
    }
}

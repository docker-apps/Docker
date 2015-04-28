
package com.docker.ui.menus;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ObjectMap;
import com.docker.Docker;
import com.docker.technicalservices.Persistence;

/**
 * overview of the statistic values and the possibility to reset them
 */
public class StatisticsMenu extends AbstractMenu {
    private TextButton resetButton = new TextButton("Reset", skin);
    private Label title = new Label("Statistics",skin, "title-white");
	private Button backButton = createBackButton(skin);

    private static Map<String,String> labelMap = new HashMap<String, String>();

    /**
     * @param application A reference to the Docker Application (Game) object.
     */
    public StatisticsMenu(final Docker application) {
    	super(application);
    	
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.returnToLastScreen();
            }
        });
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (resetButton.getText().toString().equals("Reset")) {
                    resetButton.setText("Really?");
                    resetButton.setColor(1,0,0,1);
                } else {
                    resetButton.setText("Reset");
                    resetButton.setColor(1,1,1,1);
                    Persistence.resetStatistics();
                    updateMenu(new StatisticsMenu(application));
                }
            }
        });
        setLabelMap();
        
        Table statisticsTable = new Table();
        statisticsTable.add(title).center().padBottom(10).colspan(2).row();
        loadStatistics(statisticsTable);
        Table buttonTable = new Table();
        buttonTable.add(backButton).size(100,30).expandX().center();
        buttonTable.add(resetButton).size(100,30).expandX().center();
        statisticsTable.add(buttonTable).fillX().colspan(2).padBottom(5);
        ScrollPane scrollpane = new AbstractScrollPane(statisticsTable);
        scrollpane.setPosition(0, 0);
        scrollpane.setSize(stage.getWidth(), stage.getHeight());
        scrollpane.setFlingTime(2);
        scrollpane.setupOverscroll(20, 30, 200);
        scrollpane.setFadeScrollBars(false);
        this.table.addActor(scrollpane);
    }

    /**
     * load statistics from the persistence and display them with the correct label
     * @param table the table to add the values
     */
    private void loadStatistics(Table table) {
        ObjectMap<String, Object> statisticsMap = Persistence.getStatisticsMap();
        Label h1 = new Label(labelMap.get("totalQuickScore"), skin);
        Label v1 = new Label(Persistence.getQuickHighscore().toString(), skin);
        table.add(h1).width(300).left().padBottom(5);
        table.add(v1).row().padBottom(5);
        Label h2 = new Label(labelMap.get("totalInfiniteScore"), skin);
        Label v2 = new Label(Persistence.getInfiniteHighscore().toString(), skin);
        table.add(h2).width(300).left().padBottom(5);
        table.add(v2).row().padBottom(5);
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
}

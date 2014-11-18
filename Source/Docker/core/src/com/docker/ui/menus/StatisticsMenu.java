package com.docker.ui.menus;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.docker.Docker;
import com.docker.technicalservices.Persistence;

public class StatisticsMenu implements Screen {
    Docker application;

    private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    private TextButton backButton = new TextButton("back", skin);
    private Label title = new Label("Statistics",skin);
    private Stage stage = new Stage();
    private Table table = new Table();

    private static Map<String,String> labelMap = new HashMap<String, String>();

    public StatisticsMenu(final Docker application) {
        this.application = application;

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setLastScreen();
            }
        });
        setLabelMap();

        table.add(title).padBottom(20).row();
        loadStatistics(table);
        table.add(backButton).bottom().row();

        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE) ||
                Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            application.setLastScreen();
        }

        stage.act();
        stage.draw();
    }

    private void loadStatistics(Table table) {
        ObjectMap<String, Object> statisticsMap = Persistence.getStatisticsMap();
        for (ObjectMap.Entry<String, Object> stringObjectEntry : statisticsMap) {
            Label l = new Label(labelMap.get(stringObjectEntry.key), skin);
            Label v = new Label(stringObjectEntry.value.toString(), skin);
            table.add(l).width(300).left();
            table.add(v).row().padBottom(10);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
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

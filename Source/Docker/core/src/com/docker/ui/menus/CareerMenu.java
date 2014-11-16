package com.docker.ui.menus;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.game.CareerGame;
import com.docker.technicalservices.Persistence;

public class CareerMenu implements Screen {
    Docker application;

    private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

    private Stage stage = new Stage();
    private Table table = new Table();

    private Label title = new Label("career game",skin);
    private TextButton backButton = new TextButton("back", skin);


    public CareerMenu(final Docker application) {
        this.application = application;
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setLastScreen();
            }
        });
        table.add(title).padBottom(40).row();

        addLevelButtons(table);

        table.setFillParent(true);
        stage.addActor(table);
        Gdx.input.setCatchBackKey(true);
    }

    private void addLevelButtons(Table table) {
        ArrayList<String> allLevels = Persistence.getAllLevels();
        for (final String level : allLevels) {
            TextButton button = new TextButton("Level " + level, skin);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    application.setScreen(new CareerGame(application, level));
                }
            });
            Boolean levelOpen = Persistence.isLevelOpen(level);
            if (!levelOpen) {
                button.setTouchable(Touchable.disabled);
            }
            table.add(button).padBottom(40).row();
        }
    }

    @Override
    public void render(float delta) {

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE) ||
                Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            application.setLastScreen();
        }

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);

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

    }
}

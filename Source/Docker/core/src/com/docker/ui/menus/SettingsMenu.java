package com.docker.ui.menus;


import java.math.BigDecimal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.technicalservices.Persistence;

public class SettingsMenu implements Screen {
    Docker application;
    Persistence persistence;

    private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    private TextButton backButton = new TextButton("back", skin);
    private CheckBox soundCheckBox = new CheckBox("",skin);
    private CheckBox musicCheckBox = new CheckBox("", skin);
    private Label title = new Label("Settings",skin);
    private Label volumeValue = new Label("", skin);
    Slider volumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
    private Stage stage = new Stage();
    private Table table = new Table();

    public SettingsMenu(final Docker application) {
        this.application = application;
        this.persistence = application.persistence;
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setLastScreen();
            }
        });
        soundCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Persistence.setSoundOn(!Persistence.isSoundOn());
                soundCheckBox.setChecked(Persistence.isSoundOn());
            }
        });
        musicCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Persistence.setMusicOn(!Persistence.isMusicOn());
                musicCheckBox.setChecked(Persistence.isMusicOn());
            }
        });
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Slider slider = (Slider) actor;
                Persistence.setVolume(slider.getValue());
            }
        });
        volumeSlider.setValue(Persistence.getVolume());
        soundCheckBox.setChecked(Persistence.isSoundOn());
        musicCheckBox.setChecked(Persistence.isMusicOn());
        table.add(title).left().padBottom(30).row().padBottom(20);
        table.add(new Label("sound", skin)).width(100).left();
        table.add(soundCheckBox).width(100).left().row().padBottom(20);
        table.add(new Label("music", skin)).width(100).left();
        table.add(musicCheckBox).width(100).row().padBottom(20);

        table.add(new Label("volume", skin)).width(100).left();
        table.add(volumeSlider).width(100);
        table.add(volumeValue).center().width(50).row().padBottom(20);
        table.add(backButton).size(100, 30).left().row();

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
        float volValue = Persistence.getVolume();
        BigDecimal vol = new BigDecimal(volValue*100);
        volumeValue.setText(vol.setScale(0, BigDecimal.ROUND_HALF_DOWN).toString() + "%");
        stage.act();
        stage.draw();
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
}

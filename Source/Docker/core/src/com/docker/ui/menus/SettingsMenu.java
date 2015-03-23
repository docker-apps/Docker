package com.docker.ui.menus;


import java.math.BigDecimal;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.technicalservices.Persistence;

/**
 * The menu to change settings for
 * - sound on/off
 * - music on/off
 * - music volume
 */
public class SettingsMenu extends AbstractMenu {
    Persistence persistence;

	private Button backButton = createBackButton(skin);
    private TextButton creditsButton = new TextButton("Credits", skin);
    private CheckBox soundCheckBox = new CheckBox("",skin);
    private CheckBox musicCheckBox = new CheckBox("", skin);
    private CheckBox vibrateCheckBox = new CheckBox("", skin);
    private Label title = new Label("Settings", skin, "title");
    private Label volumeValue = new Label("", skin);
    Slider volumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
    

    /**
     * @param application A reference to the Docker Application (Game) object.
     */
    public SettingsMenu(final Docker application) {
        super(application);
        
        this.persistence = application.getPersistence();
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.returnToLastScreen();
            }
        });
        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openNewMenu(new CreditsMenu(application));
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
        vibrateCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Persistence.setVibrationOn(!Persistence.isVibrationOn());
                vibrateCheckBox.setChecked(Persistence.isVibrationOn());
            }
        });
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Slider slider = (Slider) actor;
                Persistence.setVolume(slider.getValue());
            }
        });
        Table settingsTable = new Table();
        volumeSlider.setValue(Persistence.getVolume());
        soundCheckBox.setChecked(Persistence.isSoundOn());
        musicCheckBox.setChecked(Persistence.isMusicOn());
        vibrateCheckBox.setChecked(Persistence.isVibrationOn());
        settingsTable.add(title).center().padBottom(15).colspan(3).row().padBottom(10);
        settingsTable.add(new Label("Sound", skin)).width(100).left();
        settingsTable.add(soundCheckBox).width(100).left().row().padBottom(10);
        settingsTable.add(new Label("Music", skin)).width(100).left();
        settingsTable.add(musicCheckBox).width(100).left().row().padBottom(10);
        settingsTable.add(new Label("Vibrate", skin)).width(100).left();
        settingsTable.add(vibrateCheckBox).width(100).left().row().padBottom(10);

        settingsTable.add(new Label("Volume", skin)).width(100).left();
        settingsTable.add(volumeSlider).width(120);
        settingsTable.add(volumeValue).center().width(50).row().padBottom(10);
        settingsTable.add(backButton).size(100, 30).left();
        settingsTable.add(creditsButton).size(100, 30).colspan(2).center().row();
        ScrollPane scrollpane = new AbstractScrollPane(settingsTable);
        scrollpane.setPosition(0, 0);
        scrollpane.setSize(stage.getWidth(), stage.getHeight());
        scrollpane.setFlingTime(2);
        scrollpane.setupOverscroll(20, 30, 200);
        scrollpane.setFadeScrollBars(false);
        this.table.addActor(scrollpane);
    }

    /**
     * set the checkbox label according to the state of the checkbox
     * @param checkBox the checkbox to set the label
     */
    private void setCheckBoxLabel(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            checkBox.setText("On");
        } else {
            checkBox.setText("Off");
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        float volValue = Persistence.getVolume();
        BigDecimal vol = new BigDecimal(volValue*100);
        volumeValue.setText(vol.setScale(0, BigDecimal.ROUND_HALF_DOWN).toString() + "%");
        setCheckBoxLabel(musicCheckBox);
        setCheckBoxLabel(vibrateCheckBox);
        setCheckBoxLabel(soundCheckBox);
    }
}

package com.docker.ui.menus;


import java.math.BigDecimal;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.technicalservices.Persistence;

public class SettingsMenu extends AbstractMenu {
    Persistence persistence;

    private TextButton backButton = new TextButton("Back", skin);
    private TextButton creditsButton = new TextButton("Credits", skin);
    private CheckBox soundCheckBox = new CheckBox("",skin);
    private CheckBox musicCheckBox = new CheckBox("", skin);
    private Label title = new Label("Settings", skin, "title");
    private Label volumeValue = new Label("", skin);
    Slider volumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
    

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
        table.add(title).center().padBottom(15).colspan(3).row().padBottom(10);
        table.add(new Label("Sound", skin)).width(100).left();
        table.add(soundCheckBox).width(100).left().row().padBottom(10);
        table.add(new Label("Music", skin)).width(100).left();
        table.add(musicCheckBox).width(100).left().row().padBottom(10);

        table.add(new Label("Volume", skin)).width(100).left();
        table.add(volumeSlider).width(120);
        table.add(volumeValue).center().width(50).row().padBottom(10);
        table.add(backButton).size(100, 30).left();
        table.add(creditsButton).size(100, 30).colspan(2).center().row();
    }

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
        setCheckBoxLabel(soundCheckBox);
    }
}

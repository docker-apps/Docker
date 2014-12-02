package com.docker.ui.menus;


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

    private TextButton backButton = new TextButton("back", skin);
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
                volumeValue.setText(""+slider.getValue());
            }
        });
        volumeSlider.setValue(Persistence.getVolume());
        soundCheckBox.setChecked(Persistence.isSoundOn());
        musicCheckBox.setChecked(Persistence.isMusicOn());
        table.add(title).left().padBottom(15).row().padBottom(10);
        table.add(new Label("sound", skin)).width(100).left();
        table.add(soundCheckBox).width(100).left().row().padBottom(10);
        table.add(new Label("music", skin)).width(100).left();
        table.add(musicCheckBox).width(100).row().padBottom(10);

        table.add(new Label("volume", skin)).width(100).left();
        table.add(volumeSlider).width(100);
        table.add(volumeValue).center().width(50).row().padBottom(10);
        table.add(backButton).size(100, 30).left().row();
    }
}

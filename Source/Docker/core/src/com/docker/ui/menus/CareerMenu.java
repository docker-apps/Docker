package com.docker.ui.menus;

import java.util.List;

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
import com.badlogic.gdx.utils.JsonValue;
import com.docker.Docker;
import com.docker.domain.game.CareerGame;
import com.docker.technicalservices.Persistence;

public class CareerMenu extends AbstractMenu {
    private Label title = new Label("Career game",skin);
    private TextButton backButton = new TextButton("back", skin);
    

    public CareerMenu(final Docker application) {
        super(application);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setLastScreen();
            }
        });
        table.add(title).padBottom(10).row();

        addLevelButtons(table);
        
        table.add(backButton).width(80);
    }

    private void addLevelButtons(Table table) {
        List<JsonValue> allLevels = Persistence.getAllLevels();
        int i = 1;
        for (final JsonValue level : allLevels) {
            final String id = level.getString("id");
            TextButton button = new TextButton(level.getString("name"), skin);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    application.setScreen(new CareerGame(application, id));
                }
            });
            Boolean locked = Persistence.isLevelLocked(id);
            if (locked) {
                button.setTouchable(Touchable.disabled);
                button.setText("locked");
            }
            table.add(button).fillX().width(80).pad(5);
            if (i % 3 == 0) {
                table.row();
            }
            i++;
        }
    }
}

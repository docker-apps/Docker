package com.docker.ui.menus;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonValue;
import com.docker.Docker;
import com.docker.domain.game.CareerGame;
import com.docker.technicalservices.Persistence;

public class CareerMenu extends AbstractMenu {
    private Label title = new Label("Career Game",skin, "title");
    private TextButton backButton = new TextButton("Back", skin);
    

    public CareerMenu(final Docker application) {
        super(application);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.returnToLastScreen();
            }
        });
        Table careerTable = new Table();
        careerTable.setFillParent(true);
        careerTable.add(title).padBottom(10).colspan(2).row();
        addLevelButtons(careerTable);
        ScrollPane scrollpane = new ScrollPane(careerTable);
        scrollpane.setPosition(0, 0);
        scrollpane.setSize(Docker.WIDTH, Docker.HEIGHT);
        scrollpane.setFlingTime(2);
        scrollpane.setupOverscroll(20, 30, 200);
        scrollpane.setFadeScrollBars(false);
        careerTable.row();
        careerTable.add(backButton).width(80);
        this.table.addActor(scrollpane);

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
                button.setText("Locked");
            }
            table.add(button).fillX().width(80).pad(5);
            if (i % 3 == 0) {
                table.row();
            }
            i++;
        }
    }
}

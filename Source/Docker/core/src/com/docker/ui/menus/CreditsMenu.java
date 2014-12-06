package com.docker.ui.menus;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;


public class CreditsMenu extends AbstractMenu {
    private Label title = new Label("Credits", skin, "title");
    private TextButton backButton = new TextButton("Back", skin);

    public CreditsMenu(final Docker application) {
        super(application);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.returnToLastScreen();
            }
        });

        Table creditsTable = new Table();
        creditsTable.add(title).center().padBottom(10).colspan(3).row();
        creditsTable.add(new Label("Remo Höppli", skin)).row();
        creditsTable.add(new Label("Yacine Mekesser", skin)).row();
        creditsTable.add(new Label("Christoph Mathis", skin)).row();
        creditsTable.add(new Label("Emily Wangler", skin)).row();
        creditsTable.add(new Label("Music by: balbalbal", skin)).padBottom(10).row();
        creditsTable.add(backButton).left().padBottom(5).row();
        ScrollPane scrollpane = new AbstractScrollPane(creditsTable);
        scrollpane.setPosition(0, 0);
        scrollpane.setSize(this.stage.getWidth(), this.stage.getHeight());
        scrollpane.setFlingTime(2);
        scrollpane.setupOverscroll(20, 30, 200);
        scrollpane.setFadeScrollBars(false);
        this.table.addActor(scrollpane);
    }

}

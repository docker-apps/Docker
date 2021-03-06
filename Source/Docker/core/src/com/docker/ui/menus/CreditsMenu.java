package com.docker.ui.menus;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.docker.Docker;

/**
 * displays the credits for the programmer and music
 */
public class CreditsMenu extends AbstractMenu {
    private Label title = new Label("Credits", skin, "title");
	private Button backButton = createBackButton(skin);

    /**
     * @param application A reference to the Docker Application (Game) object.
     */
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
        creditsTable.add(new Label("Remo Hoeppli", skin)).row();
        creditsTable.add(new Label("Yacine Mekesser", skin)).row();
        creditsTable.add(new Label("Christoph Mathis", skin)).row();
        creditsTable.add(new Label("Emily Wangler", skin)).row();
        creditsTable.add(new Label("Music by:", skin)).row();
        creditsTable.add(new Label("'Hustle' Kevin MacLeod", skin)).row();
        creditsTable.add(new Label("(incompetech.com)", skin)).row();
        creditsTable.add(new Label("Sounds by: FreqMan", skin)).row();
        creditsTable.add(new Label("(freesound.org)", skin)).row();
        creditsTable.add(backButton).left().size(100,30).padBottom(5).row();
        ScrollPane scrollpane = new AbstractScrollPane(creditsTable);
        scrollpane.setPosition(0, 0);
        scrollpane.setSize(this.stage.getWidth(), this.stage.getHeight());
        scrollpane.setFlingTime(2);
        scrollpane.setupOverscroll(20, 30, 200);
        scrollpane.setFadeScrollBars(false);
        this.table.addActor(scrollpane);
    }

}

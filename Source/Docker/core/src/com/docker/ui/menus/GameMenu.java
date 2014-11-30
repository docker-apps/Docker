package com.docker.ui.menus;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.game.InfiniteGame;
import com.docker.domain.game.QuickGame;

/**
 *
 */
public class GameMenu extends AbstractMenu {
    private Label title = new Label("Game Modes",skin, "title");
    private TextButton careerGameButton = new TextButton("Career game", skin);
    private TextButton quickGameButton = new TextButton("Quick game", skin);
    private TextButton infiniteGameButton = new TextButton("Infinite game", skin);
    private TextButton backButton = new TextButton("Back", skin);


    public GameMenu(final Docker application) {
        super(application);
        
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setLastScreen();
            }
        });
        careerGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setScreen(new CareerMenu(application));
            }
        });
        quickGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setScreen(new QuickGame(application));
            }
        });
        infiniteGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setScreen(new InfiniteGame(application));
            }
        });

        table.add(title).padBottom(10).row();
        table.add(careerGameButton).size(150, 35).padBottom(5).row();
        table.add(quickGameButton).size(150, 35).padBottom(5).row();
        table.add(infiniteGameButton).size(150, 35).padBottom(5).row();
        table.add(backButton).size(100, 35).left();
    }
}

package com.docker.ui.menus;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.game.AbstractGame;
import com.docker.technicalservices.Persistence;

/**
 * Screen to be displayed when a running game is paused. *
 */
public class PauseMenu extends AbstractMenu {
    Persistence persistence;
    
    private TextButton endGameButton = new TextButton("Exit", skin);
    private TextButton resumeButton = new TextButton("Resume", skin);
    private TextButton retryButton = new TextButton("Retry", skin);
    private Label title = new Label("Pause",skin, "title");

    /**
     * @param application A reference to the Docker Application (Game) object.
     * @param background the background to be displayed. Should be a snapshot of the game.
     */
    public PauseMenu(final Docker application, TextureRegion background, final AbstractGame game) {
        super(application, background);
        
        this.persistence = application.getPersistence();
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.showAds(false);
                application.returnToLastScreen();
            }
        });
        endGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.showAds(true);
                application.returnToMenu(game);
            }
        });
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.startNewGame();
            }
        });

        table.add(title).center().padBottom(15).row().padBottom(10);
        table.add(resumeButton).size(100, 30).padRight(10).left();
        table.add(retryButton).size(100, 30).right().row();
        table.add(endGameButton).size(100, 30).left().row();
        stage.addActor(table);
    }
}

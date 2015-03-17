package com.docker.ui.menus;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.game.AbstractGame;
import com.docker.domain.game.TutorialGame;
import com.docker.technicalservices.Persistence;
import com.docker.technicalservices.Resource;

/**
 * Screen to be displayed when a running game is paused. *
 */
public class PauseMenu extends AbstractMenu {
    Persistence persistence;
    
    private Button endGameButton = new ImageButton(skin);
    private Button resumeButton = new ImageButton(skin);
    private Button retryButton = new ImageButton(skin);
    private Label title = new Label("Pause",skin, "title-white");

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

        Image backIconImage = new Image(Resource.getDockerSkinTextureAtlas().findRegion("resume_icon"));
        resumeButton.add(backIconImage);
        Image homeIconImage = new Image(Resource.getDockerSkinTextureAtlas().findRegion("home_icon"));
        endGameButton.add(homeIconImage);
        Image retryIconImage = new Image(Resource.getDockerSkinTextureAtlas().findRegion("retry_icon"));
        retryButton.add(retryIconImage);

        table.add(title).left().padBottom(15).row();
        table.add(resumeButton).size(50, 30).uniform();
        if (!(game instanceof TutorialGame)) {
            table.add(retryButton).size(50, 30).uniform();
        }
        table.add(endGameButton).size(50, 30).uniform();
        stage.addActor(table);
    }
}

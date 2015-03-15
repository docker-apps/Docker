package com.docker.ui.menus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.game.AbstractGame;
import com.docker.domain.game.CareerGame;

/**
 * @author HAL9000
 *
 * The screen to be displayed at the end of a game.
 * Either displays a 'You lose'-Message or the score achieved in the game.
 */
public class EndScreen extends AbstractMenu {

	private TextButton endGameButton = new TextButton("Exit", skin);
	private TextButton retryButton = new TextButton("Retry", skin);
    private Label title = new Label("Game Over",skin,"title-white");
    private TextButton nextLevelButton = new TextButton("next", skin);



    /**
     * @param application A reference to the Docker Application (Game) object.
     * @param background the background to be displayed. Should be a snapshot of the game.
     * @param score the score achieved by the player. If he has won.
     * @param highscore the highscore of the player. From the statistics.
     */
    public EndScreen(final Docker application, TextureRegion background, int score, int highscore, final AbstractGame game) {
    	super(application, background);
        
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
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CareerGame g = (CareerGame) game;
                int level = Integer.parseInt( g.getLevelId());
                String nextLevel = String.valueOf(level + 1);
                application.setScreen(new CareerGame(application, nextLevel));
            }
        });
        table.add(title).padBottom(10).colspan(3).center().row();
        table.add(new Label("Your Score", skin)).left().colspan(2);
        table.add(new Label(String.valueOf(score), skin)).colspan(2).row();
        table.add(new Label("Your Highscore", skin)).colspan(2).left();
        table.add(new Label(String.valueOf(highscore), skin)).row();
        table.add(endGameButton).size(100, 30).padTop(10).left();
        table.add(retryButton).size(100, 30).padTop(10).padLeft(5).left();
        if (game instanceof CareerGame) {
            table.add(nextLevelButton).size(100, 30).padTop(10).padLeft(5).left();
        }
    }

    /**
     * @param application A reference to the Docker Application (Game) object.
     * @param background the background to be displayed. Should be a snapshot of the game.
     */
    public EndScreen(final Docker application, TextureRegion background, final AbstractGame game) {
    	super(application, background);
        
        endGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.returnToMenu(game);
            }
        });
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.startNewGame();
            }
        });
        table.add(title).padBottom(10).center().row();
        table.add(new Label("You lose!", skin)).left().row();
        table.add(endGameButton).size(100, 30).padTop(10).left();
        table.add(retryButton).size(100, 30).padTop(10).left();
	}
}

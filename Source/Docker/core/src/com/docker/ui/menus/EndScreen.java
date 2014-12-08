package com.docker.ui.menus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;

public class EndScreen extends AbstractMenu {

	private TextButton endGameButton = new TextButton("Exit", skin);
    private Label title = new Label("Game Over",skin,"title");
    

    public EndScreen(final Docker application, TextureRegion background, int score, int highscore) {
    	super(application, background);
        
        endGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.returnToMainmenu();
            }
        });
        table.add(title).padBottom(10).colspan(2).center().row();
        table.add(new Label("Your Score", skin)).left();
        table.add(new Label(String.valueOf(score), skin)).row();
        table.add(new Label("Your Highscore", skin)).padRight(8);
        table.add(new Label(String.valueOf(highscore), skin)).row();
        table.add(endGameButton).size(100, 30).padTop(10).left().colspan(2);
	}
    
    public EndScreen(final Docker application, TextureRegion background) {
    	super(application, background);
        
        endGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.returnToMainmenu();
            }
        });
        table.add(title).padBottom(10).center().row();
        table.add(new Label("You lose!", skin)).left().row();
        table.add(endGameButton).size(100, 30).padTop(10).left();
	}
}

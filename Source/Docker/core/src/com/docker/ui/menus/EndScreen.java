package com.docker.ui.menus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;

public class EndScreen extends AbstractMenu {

	private TextButton endGameButton = new TextButton("endgame", skin);
    private Label title = new Label("Score",skin);
    

    public EndScreen(final Docker application, TextureRegion background, int score, int highscore) {
    	super(application, background);
        
        endGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setMainmenu();
            }
        });
        table.add(title).left().padBottom(15).row().padBottom(10);
        table.add(new Label("Your Score:", skin)).width(120).left();
        table.add(new Label(""+score+"", skin)).width(70).row();
        table.add(new Label("Your Highscore:", skin)).width(120).left();
        table.add(new Label(""+highscore+"", skin)).width(70);
        table.add(endGameButton).size(100, 30).left().row();
	}
    
    public EndScreen(final Docker application, TextureRegion background) {
    	super(application, background);
        
        endGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setMainmenu();
            }
        });
        table.add(title).left().padBottom(15).row().padBottom(10);
        table.add(new Label("You Lose!", skin)).width(120).left();
        table.add(endGameButton).size(100, 30).left().row();
	}
}

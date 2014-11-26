package com.docker.ui.menus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;

public class ScoreScreen extends AbstractMenu {

	private TextButton endGameButton = new TextButton("endgame", skin);
    private Label title = new Label("Score",skin);
    

    public ScoreScreen(final Docker application, TextureRegion background, int score) {
    	super(application, background);
        
        endGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setMainmenu();
            }
        });
        table.add(title).left().padBottom(15).row().padBottom(10);
        table.add(new Label("Your Score:", skin)).width(100).left();
        table.add(new Label(""+score+"", skin)).width(100);
        table.add(endGameButton).size(100, 30).left().row();
	}
}

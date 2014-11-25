package com.docker.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.technicalservices.Persistence;

public class ScoreScreen extends AbstractMenu {

	private TextButton endGameButton = new TextButton("endgame", skin);
    private TextButton resumeButton = new TextButton("back", skin);
    private Label title = new Label("Score",skin);
    

    public ScoreScreen(final Docker application, int score) {
    	super(application);
        
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

	@Override
    public void handleInput(){
    	if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE) ||
                Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            application.setLastScreen();
        }
    }
}

package com.docker.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.docker.Docker;
import com.docker.domain.game.AbstractGame;
import com.docker.technicalservices.Resource;
import com.docker.technicalservices.SoundHandler;

public class FailureEndScreen extends AbstractMenu{

	protected Label title = new Label("Game Over!", skin, "title-white");
	protected Label text;
	protected Button homeButton = createHomeButton(skin);
	protected Button retryButton = createRetryButton(skin);


	public FailureEndScreen(final Docker application, TextureRegion background, final AbstractGame game) {
		this(application, background, game, false);
	}
	
	public FailureEndScreen(final Docker application, TextureRegion background, final AbstractGame game, final boolean isCareerMode) {
		super(application, background);
		
		setMenuMusicEnabled(false);

		if(isCareerMode)
			homeButton = createIconButton(skin, "career_menu_icon");
		else
			homeButton = createHomeButton(skin);
		
		homeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundHandler.stopMusic(Resource.getEndScreenTheme());
				if(isCareerMode)
					application.returnToCareerScreen();
				else
					application.returnToMenu();
                application.showAds(true);
			}
		});
		retryButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundHandler.stopMusic(Resource.getEndScreenTheme());
				game.startNewGame();
			}
		});

		if(game.getLives() <= 0){
			text = new Label("You lost all lives.\nNext time, try to plan ahead.", skin);
		}
		else if(game.shipIsSunk()){
			text = new Label("Your ship has sunk.\nTry to achieve better balance.", skin);
		}
		else if(game.shipIsBroken()){
			text = new Label("Your ship has broken.\nMind the weight distribution.", skin);
		}
		else{
			text = new Label("", skin);
		}

		table.add(title).padBottom(10).colspan(2).left().row();
		table.add(text).colspan(2).left().row();
		table.add(homeButton).size(100, 40).padTop(10).center();
		table.add(retryButton).size(100, 40).padTop(10).center();

		SoundHandler.playSound(Resource.getSound("game_lose"));
		
		DelayAction delayAction = new DelayAction(2f);
		Action musicAction = new Action() {
			
			@Override
			public boolean act(float delta) {
				setMenuMusicEnabled(true);
				playMenuMusic();
				return true;
			}
		};
		
		SequenceAction sequence = new SequenceAction(delayAction, musicAction);
		this.stage.addAction(sequence);
	}
	
	@Override
    public void lastScreenOnReturn(){
    	if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
			SoundHandler.stopMusic(Resource.getEndScreenTheme());
			application.showAds(true);
			application.returnToMenu();
        }
    }
	
	protected void playMenuMusic(){
		if(getMenuMusicEnabled())
			SoundHandler.playMusic(Resource.getEndScreenTheme());		
	}
}

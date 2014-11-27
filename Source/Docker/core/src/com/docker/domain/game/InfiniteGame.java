package com.docker.domain.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.gameobject.Ship;

public class InfiniteGame extends AbstractGame{

	private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
	private TextButton gameMenuButton = new TextButton("Play", skin);
	private int shipsRemaining;

	public InfiniteGame(final Docker application) {
		super(application);
		Level level = Level.loadLevel();
		
		setShip(level.getShip());
		setTrain(level.getTrain());
		setLives(level.getLifeCount());
		shipsRemaining = 3;
		setLoadRating(new LoadRating(getShip().getBreakThreshold(), getShip().getCapsizeThreshold(), 1));
		

		gameMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	//TODO:Ship create und ship abfahren
            	getShip().remove();
            	setShip(Ship.getRandomShip());
            }
        });
		gameMenuButton.setPosition(50, 50);
		this.stage.addActor(gameMenuButton);	
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		if(shipsRemaining <= 0)
			super.gameOver();
		//Like that can the button be at first position
		this.stage.getBatch().begin();
		gameMenuButton.draw(this.stage.getBatch(), 1);
		this.stage.getBatch().end();
	}
}

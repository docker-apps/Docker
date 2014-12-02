package com.docker.domain.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.gameobject.Ship;
import com.docker.technicalservices.Persistence;
import com.docker.ui.menus.AbstractMenu;

public class InfiniteGame extends AbstractGame{

	private Skin skin = AbstractMenu.getDockerSkin();
	private Button gameMenuButton;
	private int remainingShips;
	private boolean newShip = false;

	public InfiniteGame(final Docker application) {
		super(application);
		Level level = Level.loadLevel();
		
		setShip(level.getShip());
		setTrain(level.getTrain());
		setLives(level.getLifeCount());
		setRemainingShips(3);
		setLoadRating(new LoadRating(getShip().getBreakThreshold(), getShip().getCapsizeThreshold(), 1));
		
		gameMenuButton = new Button(skin.get("ship-button", ButtonStyle.class));
		gameMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	//TODO:Ship check break and sink
            	getShip().takeOff();
            	newShip = true;
            }
        });
		gameMenuButton.setPosition(10, getStage().getHeight() - gameMenuButton.getHeight() - 60);
		this.stage.addActor(gameMenuButton);	
	}
	
	@Override
	public boolean canPlayerAct() {
		if(getShip().isTakingOff){
			return false;
		}
		return super.canPlayerAct();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		if(getRemainingShips() <= 0)
			super.gameOver();
		if(!getShip().isTakingOff() && newShip){
        	getShip().remove();
        	setShip(Ship.getRandomShip());
        	newShip = false;
		}
		if(train.getContainerListSize()<= 5)
			train.addContainer(Level.createRandomContainer());
		//Like that can the button be at first position
		this.stage.getBatch().begin();
		gameMenuButton.draw(this.stage.getBatch(), 1);
		this.stage.getBatch().end();
	}

    @Override
    public Integer endGame(Integer gameScore) {
        Integer highscore = Persistence.getInfiniteHighscore();
        if (highscore < gameScore) {
            Persistence.setInfiniteHighscore(gameScore);
            return gameScore;
        }
        return highscore;
    }

    public int getRemainingShips() {
		return remainingShips;
	}

	public void setRemainingShips(int remainingShips) {
		this.remainingShips = remainingShips;
		this.foreground.setRemainingShips(remainingShips);
	}
}

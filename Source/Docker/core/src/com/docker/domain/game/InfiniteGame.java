package com.docker.domain.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.gameobject.Ship;
import com.docker.technicalservices.Persistence;
import com.docker.technicalservices.Resource;

public class InfiniteGame extends AbstractGame{

	private Skin skin = Resource.getDockerSkin();
	private Button gameMenuButton;

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
            	checkShipCondition();
            	setShip(Ship.getRandomShip());
            	getShip().runIn();            	
            }
        });
		gameMenuButton.setPosition(10, getStage().getHeight() - gameMenuButton.getHeight() - 60);
		this.stage.addActor(gameMenuButton);	
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		if(getCrane().isDeploying()){
			gameMenuButton.setTouchable(Touchable.disabled);
		}else{
			gameMenuButton.setTouchable(Touchable.enabled);
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
    
    @Override
    public void displayEndScreen(boolean isGameLost){
    	super.displayEndScreen(false);
    }

    @Override
	public void setRemainingShips(int remainingShips) {
		super.setRemainingShips(remainingShips);
		this.foreground.setRemainingShips(remainingShips);
	}

    @Override
    public void startNewGame() {
        application.returnToLastScreen();
        application.updateScreen(new InfiniteGame(application));
    }
}

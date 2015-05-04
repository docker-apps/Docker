package com.docker.domain.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.docker.Docker;
import com.docker.domain.gameobject.Container;
import com.docker.domain.gameobject.Ship;
import com.docker.domain.gameobject.shipskins.ShipSkinManager;
import com.docker.technicalservices.Persistence;
import com.docker.technicalservices.Resource;
import com.docker.ui.menus.InfiniteSuccessEndScreen;

public class InfiniteGame extends AbstractGame{

	private static final float TRAIN_SPEEDUP_RATE = 0.05f;
	
	protected int score;
	protected int shipCounter;
	private Skin skin = Resource.getDockerSkin();
	private Button goButton;
	private Label scoreTitle = new Label("Score: ", skin);;
	private Label scoreLabel = new Label("0", skin);;

	public InfiniteGame(final Docker application) {
		super(application);
		Level level = Level.loadLevel();
		
		setShip(level.getShip());
		setTrain(level.getTrain());
		setLives(level.getLifeCount());
		setRemainingShips(3);
		setLoadRating(new LoadRating(getShip().getBreakThreshold(), getShip().getCapsizeThreshold(), 1));
		
		setScore(0);
		
		goButton = new Button(skin.get("ship-button", ButtonStyle.class));
		goButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	checkShipCondition();
            	if(!shipIsBroken() &&!shipIsSunk()){
            		addToScore(getLoadRating().getScore());
            		increaseShipCounter();
            	}
            	setShip(Ship.getRandomShip(ShipSkinManager.getConfiguredSkin()));
            	getShip().runIn();            	
            }
        });
		
		initUiPositions();

		this.stage.addActor(scoreTitle);
		this.stage.addActor(scoreLabel);
		this.stage.addActor(goButton);
	}
	
	private void initUiPositions(){
		goButton.setPosition(10, getStage().getHeight() - goButton.getHeight() - 60);
		
		scoreTitle.setPosition(
				goButton.getX() + goButton.getWidth() + 10f,
				getStage().getHeight() - 60f);
		scoreLabel.setPosition(
				scoreTitle.getX() + scoreTitle.getTextBounds().width,
				scoreTitle.getY());
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
		//the train gets continually faster
		getTrain().setSpeed(getTrain().getSpeed() + TRAIN_SPEEDUP_RATE * delta);
		
		if(getCrane().isDeploying()){
			goButton.setTouchable(Touchable.disabled);
		}else{
			goButton.setTouchable(Touchable.enabled);
		}
		
		Container lastContainer = train.getLastContainer();
		if(lastContainer.getX() >= -(lastContainer.getWidth()+20f))
			train.addContainer(Level.createRandomContainer());
		
		//so the ui stuff is drawn at topmost position
		this.stage.getBatch().begin();
		goButton.draw(this.stage.getBatch(), 1);
		scoreTitle.draw(this.stage.getBatch(), 1);
		scoreLabel.draw(this.stage.getBatch(), 1);
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
    	stopAllSound();
    	application.showInterstital();
		TextureRegion screenCap = ScreenUtils.getFrameBufferTexture();
		getLoadRating().getCapsizeValue();
		Integer highscore = endGame(getScore());
		application.setScreen(new InfiniteSuccessEndScreen(application, screenCap, this, getScore(), highscore));
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
        playAllSound();
    }

	/**
	 * @return the current score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the current score
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	public void updateScoreLabel(){
		this.scoreLabel.setText(""+getScore());
	}
	
	public void addToScore(final int points) {
		setScore(getScore() + points);
		
		final Label score = new Label(""+points, skin);
		score.setPosition(
				getShip().getX() + getShip().getWidth() - score.getWidth(),
				getShip().getY() + getShip().getHeight()/2);
		
		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(
				scoreLabel.getX() + scoreLabel.getTextBounds().width/2, 
				scoreLabel.getY());
		moveAction.setDuration(1f);
		moveAction.setInterpolation(Interpolation.pow2In);
		
		Action addPointsAction = new Action() {
			
			@Override
			public boolean act(float delta) {
				score.remove();
				updateScoreLabel();
				
				scoreLabel.setFontScale(1.5f);
				Action sizeDown = new Action() {
					
					@Override
					public boolean act(float delta) {
						float scale = scoreLabel.getFontScaleX();
						if(scale <= 1f){
							scoreLabel.setFontScale(1);
							return true;
						}
						scoreLabel.setFontScale(scale - delta*3f);
						return false;
					}
				};
				
				scoreLabel.addAction(sizeDown);
				return true;
			}
		};
		
		SequenceAction sequence = new SequenceAction(moveAction, addPointsAction);
		score.addAction(sequence);
		getStage().addActor(score);
	}

	public int getShipCounter() {
		return shipCounter;
	}

	public void setShipCounter(int shipCounter) {
		this.shipCounter = shipCounter;
	}
	
	public void increaseShipCounter() {
		setShipCounter(getShipCounter() + 1);
	}
	
	@Override
	public void resize(int width, int height){
		super.resize(width, height);
		this.initUiPositions();
	}
}

package com.docker.domain.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.docker.Docker;
import com.docker.domain.world.Background;
import com.docker.domain.world.Foreground;
import com.docker.domain.world.LondonBackground;
import com.docker.domain.world.RainyForeground;
import com.docker.technicalservices.Persistence;
import com.docker.ui.menus.CareerSuccessEndScreen;
import com.docker.ui.menus.FailureEndScreen;
import com.docker.ui.menus.PauseMenu;

/**
 * The goal in the career game is to unlock all levels
 * the levels are defined in assets/level/level.json
 */
public class CareerGame extends AbstractGame {
	private String packageId;
    private String levelId;

	public CareerGame(Docker application, String levelId) {
        super(application);
        this.levelId = levelId;
        this.packageId = Persistence.getPackageId(levelId);

		Level level = Level.loadLevel(levelId);
		this.setLives(level.getLifeCount());
		this.setShip(level.getShip());
		this.setTrain(level.getTrain());
		this.setLives(level.getLifeCount());
		setLoadRating(new LoadRating(ship.getBreakThreshold(), ship.getCapsizeThreshold(), 1));
		
		if(packageId.equals("2")){
			Background bg = new LondonBackground(stage.getWidth(), stage.getHeight());
			this.background = bg;
			stage.setBackground(bg);
			Foreground fg = new RainyForeground(stage);
			this.foreground = fg;
			stage.setForeground(fg);
		}
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}

    /**
     * unlocks next level and sets highscore per level
     *
     * @param gameScore the score of the current game
     * @return the new highscore
     */
    @Override
    public Integer endGame(Integer gameScore) {
        int level = Integer.parseInt(levelId);
        Persistence.unlockLevel(String.valueOf(level + 1));
        Integer levelHighScore = Persistence.getLevelScore(levelId);
        if (gameScore > levelHighScore) {
            Persistence.setLevelScore(levelId, gameScore);
            return gameScore;
        }
        return levelHighScore;
    }

    @Override
    public void displayEndScreen(boolean isGameLost){
        application.showInterstital();
		TextureRegion screenCap = ScreenUtils.getFrameBufferTexture();
		//application.setScreen(new EndScreen(application, screenCap));
        if(!isGameLost){
        	getLoadRating().getCapsizeValue();
            Integer gameScore = getLoadRating().getScore();
            Integer highscore = endGame(gameScore);
            application.setScreen(new CareerSuccessEndScreen(application, screenCap, this, gameScore, highscore));
        } else{
        	application.setScreen(new FailureEndScreen(application, screenCap, this, true));
        }
    }

    @Override
    public void startNewGame() {
        application.returnToLastScreen();
        application.updateScreen(new CareerGame(application, levelId));
    }
    
    @Override
	protected void displayPauseScreen() {
		TextureRegion screenCap = ScreenUtils.getFrameBufferTexture();			
		application.setScreen(new PauseMenu(application, screenCap, this, true));
	}

    public String getLevelId() {
        return levelId;
    }
}

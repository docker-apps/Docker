package com.docker;

import java.util.Stack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.docker.technicalservices.Persistence;
import com.docker.technicalservices.Resource;
import com.docker.ui.menus.AbstractMenu;
import com.docker.ui.menus.CareerMenu;
import com.docker.ui.menus.MainMenu;

/**
 * @author HAL9000
 *
 * Represents the application Docker. 
 * Manages the different screens and, in extension to the libGDX Game-class, 
 * it keeps a history for the screens, so you can easily return through the menu history.
 */
public class Docker extends Game {
	/**
	 * The default x-resolution for the application.
	 */
	public static final float WIDTH = 360;

	/**
	 * The default y-resolution for the application.
	 */
	public static final float HEIGHT = 200;

    /**
     * Count played games to show interstitial ad
     */
    public static Integer GAMES_PLAYED = 0;

	private final AdController adController;

	Stack<Screen> history;
	private Persistence persistence;

	public Docker(AdController adController) {
		this.adController = adController;
	}

	@Override
	public void create() {
		this.history = new Stack<Screen>();
		this.setPersistence(new Persistence());
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void setScreen(Screen screen){
		if(this.getScreen() != null){
			history.push(this.getScreen());
		}
		super.setScreen(screen);
	}

	/**
	 * Identical to setScreen, but the old screen will not be saved in the screen history.
	 *
	 * @param screen the new screen.
	 */
	public void updateScreen(Screen screen){
		Screen old = this.getScreen();
		super.setScreen(screen);
		old.dispose();
	}

	/**
	 * Return to the last screen.
	 * The current screen will be disposed.
	 */
	public void returnToLastScreen(){
		if(history.size() > 0){
			Screen old = this.getScreen();
			super.setScreen(history.pop());
			// temporarly removed, as the application crashes here.
			old.dispose();
		}
	}

	/**
	 * Return to the MainMenu screen.
	 * The history will be discarded.
	 */
	public void returnToMenu() {
		while (history.size() > 1)
			history.pop().dispose();
		super.setScreen(history.pop());
	}
	
	/**
	 * Return to the MainMenu screen.
	 * The history will be discarded.
	 */
	public void returnToCareerScreen() {
		returnToMenu();
		((AbstractMenu)this.getScreen()).openNewMenu(new CareerMenu(this));
	}

	public void showAds(Boolean showAds) {
		adController.showAds(showAds);
	}

	public void showInterstital() {
        int gamesPlayed = Docker.GAMES_PLAYED;
        if (gamesPlayed % 4 == 0) {
            adController.showInterstitialAd();
        }
        Docker.GAMES_PLAYED++;
	}

	/**
	 * @return an instance of the persistence interface
	 */
	public Persistence getPersistence() {
		return persistence;
	}

	/**
	 * @param persistence the instance of the persistence interface
	 */
	public void setPersistence(Persistence persistence) {
		this.persistence = persistence;
	}

	@Override
	public void dispose () {
		super.dispose();
		for (Screen screen : history) {
			screen.dispose();
		}
		Resource.disposeAll();
	}

}

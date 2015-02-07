package com.docker;

import java.util.Stack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.docker.technicalservices.Persistence;
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
	
	Stack<Screen> history;
    private Persistence persistence;
	
	@Override
	public void create() {
		this.history = new Stack<Screen>();
		this.setScreen(new MainMenu(this));
        this.setPersistence(new Persistence());
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
		super.setScreen(screen);
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
			//old.dispose();
		}
	}
	
	/**
	 * Return to the MainMenu screen.
	 * The history will be discarded.
	 */
	public void returnToMainmenu(){
		while(history.size() > 0)
			history.pop().dispose();
		super.setScreen(new MainMenu(this));
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

}

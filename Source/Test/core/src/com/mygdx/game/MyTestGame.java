package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class MyTestGame extends Game {

	Screen lastScreen;
	
	@Override
	public void create() {
		this.setScreen(new MainMenu(this));
	}
	
	@Override
	public void setScreen(Screen screen){
		this.lastScreen = this.getScreen();
		super.setScreen(screen);
	}
	
	public void setLastScreen(){
		this.setScreen(this.lastScreen);
	}

}

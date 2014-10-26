package com.docker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.docker.ui.menus.MainMenu;

public class Docker extends Game {
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

package com.docker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.docker.technicalservices.Persistence;
import com.docker.ui.menus.MainMenu;

public class Docker extends Game {
	public static final float WIDTH = 360;
	public static final float HEIGHT = 200;
	
	Screen lastScreen;
    public Persistence persistence;
	
	@Override
	public void create() {
		this.setScreen(new MainMenu(this));
        this.persistence = new Persistence();
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

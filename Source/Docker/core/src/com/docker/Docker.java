package com.docker;

import java.util.Stack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.docker.technicalservices.Persistence;
import com.docker.ui.menus.MainMenu;

public class Docker extends Game {
	public static final float WIDTH = 360;
	public static final float HEIGHT = 200;
	
	Stack<Screen> history;
    public Persistence persistence;
	
	@Override
	public void create() {
		this.history = new Stack<Screen>();
		this.setScreen(new MainMenu(this));
        this.persistence = new Persistence();
	}
	
	@Override
	public void setScreen(Screen screen){
		if(this.getScreen() != null){
			history.push(this.getScreen());
		}
		super.setScreen(screen);
	}
	
	public void setLastScreen(){
		if(history.size() > 0)
			super.setScreen(history.pop());
	}
}

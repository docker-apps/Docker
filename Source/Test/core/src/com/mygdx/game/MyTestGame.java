package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyTestGame extends Game {

	@Override
	public void create() {
		this.setScreen(new MainMenu(this));
	}

}

package com.docker.technicalservices;

import com.badlogic.gdx.audio.Sound;

public class SoundHandler {
	
	public static void playSound(Sound sound){
		if(Persistence.isSoundOn()){
			sound.play(Persistence.getVolume());
		}
	}
}

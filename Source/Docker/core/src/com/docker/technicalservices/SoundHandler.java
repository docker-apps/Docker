package com.docker.technicalservices;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundHandler {
	
	public static void playSound(Sound sound){
		if(isSoundOn()){
			sound.play(getVolume());
		}
	}
	
	public static void playAmbient(Music music){
		playAmbient(music, true);
	}
	
	public static void playAmbient(Music track, boolean looping){
		if(isSoundOn()){
			track.setVolume(getVolume());
			track.setLooping(looping);
			track.play();
		}
	}
	
	public static void playMusic(Music music){
		playMusic(music, true);
	}
	
	public static void playMusic(Music music, boolean looping){
		if(isMusicOn()){
			music.setVolume(getVolume());
			music.setLooping(looping);
			music.play();
		}
	}
	
	public static void pauseMusic(Music music){
		music.pause();
	}
	
	public static void pauseAmbient(Music track){
		pauseMusic(track);
	}
	
	public static void stopMusic(Music music){
		music.stop();
	}
	
	public static void stopAmbient(Music track){
		stopMusic(track);
	}
	
	private static boolean isSoundOn(){
		return Persistence.isSoundOn();
	}
	
	private static boolean isMusicOn(){
		return Persistence.isMusicOn();
	}
	
	private static float getVolume(){
		return Persistence.getVolume();
	}
}

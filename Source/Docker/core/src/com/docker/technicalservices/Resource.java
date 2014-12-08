package com.docker.technicalservices;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public class Resource {
	
	private static TextureAtlas dockerAtlas;
	private static TextureAtlas dockerSkinAtlas;
	
	public static AtlasRegion findRegion(String label){
		return getDockerTextureAtlas().findRegion(label);
	}
	
	public static Array<AtlasRegion> findRegions(String label){
		return getDockerTextureAtlas().findRegions(label);
	}
	
	public static TextureAtlas getDockerTextureAtlas(){
		if(dockerAtlas == null){
			dockerAtlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));
		}
		return dockerAtlas;		
	}
	
	public static TextureAtlas getDockerSkinTextureAtlas(){
		if(dockerSkinAtlas == null){
			dockerSkinAtlas = new TextureAtlas(Gdx.files.internal("ui/dockerskin.atlas"));
		}
		return dockerSkinAtlas;		
	}
	
	public static void disposeAll(){
		dockerAtlas.dispose();
		dockerSkinAtlas = dockerAtlas;
		dockerSkinAtlas.dispose();
		dockerSkinAtlas = null;
	}
}

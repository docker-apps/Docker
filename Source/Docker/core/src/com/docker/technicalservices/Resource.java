package com.docker.technicalservices;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * @author HAL9000
 *
 * Handles the resources used by the game, such as TextureAtlases and Skins.
 * Loads the resources lazily and provides a single method to dispose all of them.
 */
public class Resource {
	
	private static TextureAtlas dockerAtlas;
	private static TextureAtlas dockerSkinAtlas;
	private static Skin dockerSkin;
	
	/**
	 * Returns the first region in the Docker TextureAtlas found with the specified name.
	 * 
	 * @param label the name of the region
	 * @return The region, or null.
	 */
	public static AtlasRegion findRegion(String label){
		return getDockerTextureAtlas().findRegion(label);
	}

	/**
	 * Returns all regions in the Docker TextureAtlas with the specified name, 
	 * ordered by smallest to largest index. 
	 * 
	 * @param label the name of the regions
	 * @return An array with the regions, or null.
	 */
	public static Array<AtlasRegion> findRegions(String label){
		return getDockerTextureAtlas().findRegions(label);
	}
	
	/**
	 * @return the main TextureAtlas for the game.
	 */
	public static TextureAtlas getDockerTextureAtlas(){
		if(dockerAtlas == null){
			dockerAtlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));
		}
		return dockerAtlas;		
	}
	
	/**
	 * @return the main TextureAtlas for the ui elements.
	 */
	public static TextureAtlas getDockerSkinTextureAtlas(){
		if(dockerSkinAtlas == null){
			dockerSkinAtlas = new TextureAtlas(Gdx.files.internal("ui/dockerskin.atlas"));
		}
		return dockerSkinAtlas;		
	}
	
	/**
	 * @return the custom skin used by docker.
	 */
	public static Skin getDockerSkin(){
		if(dockerSkin == null){
			dockerSkin = new Skin(Gdx.files.internal("ui/dockerskin.json"));
		}		
		return dockerSkin;
	}
	
	/**
	 * Dispose of all the resources.
	 */
	public static void disposeAll(){
		dockerAtlas.dispose();
		dockerAtlas = null;
		dockerSkinAtlas.dispose();
		dockerSkinAtlas = null;
		dockerSkin.dispose();
		dockerSkin = null;
	}
}

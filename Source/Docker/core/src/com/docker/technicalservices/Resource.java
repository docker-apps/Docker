package com.docker.technicalservices;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;

public class Resource {
	// TODO: Analysieren ob Resource mit Assetmanager ersetzt werden soll.
	// TODO: Analysieren ob Resourcen nicht besser anstatt static an das Docker-Objekt gehängt werden soll.
	
	private static TextureAtlas dockerAtlas;
	private static TextureAtlas dockerSkinAtlas;
	private static Skin dockerSkin;
	
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
	
	/**
	 * @return the custom skin used by docker.
	 */
	public static Skin getDockerSkin(){
		if(dockerSkin == null){
			dockerSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
			dockerSkin.add("default", new BitmapFont(Gdx.files.internal("ui/rokkitt_28.fnt")));
			dockerSkin.add("big", new BitmapFont(Gdx.files.internal("ui/rokkitt_32.fnt")));

			TextureAtlas atlas = Resource.getDockerSkinTextureAtlas();
			dockerSkin.addRegions(atlas);

			TextButtonStyle textButtonStyle = new TextButtonStyle();
			textButtonStyle.up = dockerSkin.getDrawable("yellow_button");
			textButtonStyle.down = dockerSkin.getDrawable("yellow_button_pressed");
			textButtonStyle.font = dockerSkin.getFont("default");
			dockerSkin.add("default", textButtonStyle);

			LabelStyle labelStyle = new LabelStyle();
			labelStyle.font = dockerSkin.getFont("default");
			dockerSkin.add("default", labelStyle);

			LabelStyle labelStyleTitle = new LabelStyle();
			labelStyleTitle.font = dockerSkin.getFont("big");
			labelStyleTitle.fontColor = new Color(0f, 0.165f, 0.322f, 1);
			dockerSkin.add("title", labelStyleTitle);

			CheckBoxStyle checkBoxStyle = new CheckBoxStyle();
			checkBoxStyle.checkboxOn = dockerSkin.getDrawable("livesaver_off");
			checkBoxStyle.checkboxOff = dockerSkin.getDrawable("livesaver_on");
			checkBoxStyle.font = dockerSkin.getFont("default");
			dockerSkin.add("default", checkBoxStyle);

			SliderStyle sliderStyle = new SliderStyle();
			sliderStyle.knob = dockerSkin.getDrawable("slider_pointer");
			sliderStyle.background = dockerSkin.getDrawable("slider_rail");
			dockerSkin.add("default-horizontal", sliderStyle);

			ButtonStyle shipButtonStyle = new ButtonStyle();
			shipButtonStyle.up = dockerSkin.getDrawable("ship_button_up");
			shipButtonStyle.down = dockerSkin.getDrawable("ship_button_down");
			dockerSkin.add("ship-button", shipButtonStyle);
		}		
		return dockerSkin;
	}
	
	public static void disposeAll(){
		dockerAtlas.dispose();
		dockerAtlas = null;
		dockerSkinAtlas.dispose();
		dockerSkinAtlas = null;
		dockerSkin.dispose();
		dockerSkin = null;
	}
}

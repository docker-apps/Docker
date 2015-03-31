package com.docker.technicalservices;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
	private static ShaderProgram snapshotShader;
	private static Texture rainTexture;
	private static Texture smokePuffTexture;

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

	public static ShaderProgram getSnapshotShader(){
		if(snapshotShader == null){
			// No idea how this shader works, but it is needed to blend alpha values correctly.
			String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
					+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
					+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
					+ "uniform mat4 u_projTrans;\n" //
					+ "varying vec4 v_color;\n" //
					+ "varying vec2 v_texCoords;\n" //
					+ "\n" //
					+ "void main()\n" //
					+ "{\n" //
					+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
					+ "   v_color.a = v_color.a * (256.0/255.0);\n" //
					+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
					+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
					+ "}\n";
			String fragmentShader = "#ifdef GL_ES\n" //
					+ "#define LOWP lowp\n" //
					+ "precision mediump float;\n" //
					+ "#else\n" //
					+ "#define LOWP \n" //
					+ "#endif\n" //
					+ "varying LOWP vec4 v_color;\n" //
					+ "varying vec2 v_texCoords;\n" //
					+ "uniform sampler2D u_texture;\n" //
					+ "void main()\n"//
					+ "{\n" //
					+ "  vec4 initialColor = v_color * texture2D(u_texture, v_texCoords);\n" //
					+ "  gl_FragColor = vec4(initialColor.rgb * initialColor.a, initialColor.a);\n" //
					+ "}";
			snapshotShader = new ShaderProgram(vertexShader, fragmentShader);
			if (!snapshotShader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + snapshotShader.getLog());
		}
		return snapshotShader;
	}

	public static Texture getRainTexture(){
		if(Resource.rainTexture == null){
			Pixmap rainPixmap;
			rainPixmap = new Pixmap(4, 24, Format.RGBA8888);
			Pixmap.setFilter(Filter.NearestNeighbour);
			Pixmap.setBlending(Blending.None);
			rainPixmap.setColor(new Color(1f, 1f, 1f, 0.2f));
			rainPixmap.drawLine(0, 0, 1, 6);
			rainPixmap.setColor(new Color(1f, 1f, 1f, 0.4f));
			rainPixmap.drawLine(1, 6, 2, 12);
			rainPixmap.setColor(new Color(1f, 1f, 1f, 0.6f));
			rainPixmap.drawLine(2, 12, 3, 18);
			rainPixmap.setColor(new Color(1f, 1f, 1f, 0.8f));
			rainPixmap.drawLine(3, 18, 4, 24);
			Resource.rainTexture = new Texture(rainPixmap);
			rainPixmap.dispose();
		}
		return Resource.rainTexture;
	}
	
	public static Texture getSmokePuffTexture(){
		if(Resource.smokePuffTexture == null){
			Pixmap pixmap = new Pixmap(11, 11, Format.RGBA4444);
			Pixmap.setBlending(Blending.None);
			pixmap.setColor(Color.WHITE);
			pixmap.fillCircle(5, 5, 5);

			Resource.smokePuffTexture = new Texture(pixmap);
			pixmap.dispose();
		}
		return Resource.smokePuffTexture;
	}

	/**
	 * Dispose of all the resources.
	 */
	public static void disposeAll(){
		if(dockerAtlas != null){
			dockerAtlas.dispose();
			dockerAtlas = null;
		}
		if(dockerSkinAtlas != null){
			dockerSkinAtlas.dispose();
			dockerSkinAtlas = null;
		}
		if(dockerSkin != null){
			dockerSkin.dispose();
			dockerSkin = null;
		}
		if(snapshotShader != null){
			snapshotShader.dispose();
			snapshotShader = null;
		}
		if(rainTexture != null){
			rainTexture.dispose();
			rainTexture = null;
		}
		if(smokePuffTexture != null){
			smokePuffTexture.dispose();
			smokePuffTexture = null;
		}
	}
}

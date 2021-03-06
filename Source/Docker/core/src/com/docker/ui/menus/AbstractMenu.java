package com.docker.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.docker.Docker;
import com.docker.technicalservices.Resource;
import com.docker.technicalservices.SoundHandler;
import com.docker.technicalservices.WorldStage;

/** 
 * Abstracton for all Menus. Sets up a stage and a table as well as the basic input handling and rendering.
 * 
 * @author HAL9000
 *
 */
public class AbstractMenu implements Screen {
	Docker application;
	
    protected Skin skin;

    protected WorldStage stage;
    protected ExtendViewport viewport;
    protected Table table;
    
    protected boolean menuMusicEnabled = true;
    
    /**
     * Default Constructor for Menus.
     * 
     * @param application A reference to the Docker Application (Game) object.
     */
    public AbstractMenu(final Docker application){
    	this.application = application;
		
    	this.skin = Resource.getDockerSkin();
		this.viewport = new ExtendViewport(Docker.WIDTH, Docker.HEIGHT);
		this.stage = new WorldStage(viewport);
		this.table = new Table();
		
        table.setFillParent(true);
        stage.addActor(table);
    }
    
    /**
     * Default Constructor for Menus with a background object.
     * Can be used for seamless menu transitions with animated backgrounds.
     * 
     * @param application A reference to the Docker Application (Game) object.
     * @param background The background object to display in the menu. 
     */
    public AbstractMenu(final Docker application, Actor background){
    	this(application);
    	this.setBackground(background);
    }

    
    /**
     * Default Constructor for Menus.
     * Can be used for pause screens, where the background should be a static image (snapshot) of the game.
     * 
     * @param application A reference to the Docker Application (Game) object.
     * @param background A textureRegion to be used as the background
     */
    public AbstractMenu(final Docker application, TextureRegion background){
    	this(application);

        Image bg = new Image(background);
        bg.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        bg.setPosition(0f, 0f);
        bg.setColor(1f, 1f, 1f, 0.5f);
    	this.setBackground(bg);
    }
    
    /**
     * Hook-Operation which will be called every render cycle. Override to individually handle input.
     * 
     * Calls lastScreenOnReturn() by default
     */
    public void handleInput(){
    	lastScreenOnReturn();
    }
    
    /**
     * Returns to the last screen on Escape or Return-Key
     */
    public void lastScreenOnReturn(){
    	if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            application.returnToLastScreen();
        }
    }
    
    /**
     * Opens a new menu.
     * Saves the old menu in the screen history.
     * 
     * @param menu the new menu.
     */
    public void openNewMenu(AbstractMenu menu){
        this.application.setScreen(menu);
        menu.setBackground(getBackground());
    }
    
    /**
     * Updates the current menu with a new one.
     * Does NOT save the old menu in the screen history.
     * 
     * @param menu the new menu.
     */
    public void updateMenu(AbstractMenu menu){
    	this.application.updateScreen(menu);
    	menu.setBackground(getBackground());
    }
    
    /**
     * Sets the Background to be displayed in the Menu
     * 
     * @param bg the actor object to be displayed as a background.
     */
    public void setBackground(Actor bg){
        this.stage.setBackground(bg);
    }
    
    /**
     * @return the actor object used as a background.
     */
    public Actor getBackground(){
    	return this.stage.getBackground();
    }

    @Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        
        // handle input after rendering, as a consequence of the input-handling
        // can be the disposing of relevant resources.
    	handleInput();
	}
    
    protected static Button createIconButton(Skin skin, String iconRegionLabel){
    	ImageButton button = new ImageButton(skin);
    	Image iconImage = new Image(Resource.getDockerSkinTextureAtlas().findRegion(iconRegionLabel));
    	button.add(iconImage);
		return button;
    }
    
    protected static Button createBackButton(Skin skin){
    	return createIconButton(skin, "back_icon");
    }
    
    protected static Button createResumeButton(Skin skin){
    	return createIconButton(skin, "resume_icon");
    }
    
    protected static Button createHomeButton(Skin skin){
    	return createIconButton(skin, "home_icon");
    }
    
    protected static Button createRetryButton(Skin skin){
    	return createIconButton(skin, "retry_icon");
    }
    
    protected static Button createNextLevelButton(Skin skin){
    	return createIconButton(skin, "next_level_icon");
    }

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		if(stage.getBackground() instanceof MenuBackground){
			((MenuBackground) stage.getBackground()).setStage(stage);
		}
	}

	@Override
	public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);

		playMenuMusic();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
        SoundHandler.pauseMusic(Resource.getMenuTheme());
	}

	@Override
	public void resume() {
		playMenuMusic();
	}

	@Override
	public void dispose() {
        stage.dispose();
	}
	
	protected void playMenuMusic(){
		if(getMenuMusicEnabled())
			SoundHandler.playMusic(Resource.getMenuTheme());		
	}
	
	public void setMenuMusicEnabled(boolean menuMusicEnabled){
		this.menuMusicEnabled = menuMusicEnabled;
	}
	
	public boolean getMenuMusicEnabled(){
		return this.menuMusicEnabled;
	}

    /**
     * @author HAL9000
     *
     * Menu Container control for scrolling menus.
     */
    public class AbstractScrollPane extends ScrollPane {
        ShapeRenderer shapeRenderer = new ShapeRenderer();

        /**
         * @param widget the menu control to be wrapped in a scroll pane. May be null.
         */
        public AbstractScrollPane(Actor widget){
            super(widget);
        }

        @Override
        public void draw(Batch batch, float parentAlpha){
            batch.end();
            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 0f, 0f, 0.4f);
            shapeRenderer.rect(0f, 0f, this.getStage().getWidth(), this.getStage().getHeight());
            shapeRenderer.end();
            batch.begin();

            super.draw(batch, parentAlpha);
        }
    }
    
    protected class ClickListener extends com.badlogic.gdx.scenes.scene2d.utils.ClickListener{
		@Override
		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			SoundHandler.playSound(Resource.getSound("button_click"));
			return super.touchDown(event, x, y, pointer, button);
		}
	}
}

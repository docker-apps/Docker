package com.docker.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.docker.Docker;
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
    
    public AbstractMenu(final Docker application){
    	this.application = application;
		
    	this.skin = AbstractMenu.getDockerSkin();
		this.viewport = new ExtendViewport(Docker.WIDTH, Docker.HEIGHT);
		this.stage = new WorldStage(viewport);
		this.table = new Table();
		
        table.setFillParent(true);
        stage.addActor(table);
    }
    
    public AbstractMenu(final Docker application, Actor background){
    	this(application);
    	this.setBackground(background);
    }
    
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
    
    public void openNewMenu(AbstractMenu menu){
        this.application.setScreen(menu);
        menu.setBackground(getBackground());
    }
    
    /**
     * Sets the Background to be displayed in the Menu
     * 
     * @param texture the texture to be displayed as a background.
     */
    public void setBackground(Actor bg){
        this.stage.setBackground(bg);
    }
    
    public Actor getBackground(){
    	return this.stage.getBackground();
    }

    @Override
	public void render(float delta) {
    	handleInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
        stage.dispose();
        skin.dispose();
	}

	/**
	 * @return the custom skin used by docker.
	 */
	public static Skin getDockerSkin(){
		Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		skin.add("default", new BitmapFont(Gdx.files.internal("ui/rokkitt.fnt")));
		skin.add("big", new BitmapFont(Gdx.files.internal("ui/rokkitt_32.fnt")));

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/dockerskin.atlas"));
		skin.addRegions(atlas);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("yellow_button");
		textButtonStyle.down = skin.getDrawable("yellow_button_pressed");
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("default");
		skin.add("default", labelStyle);
		
		LabelStyle labelStyleTitle = new LabelStyle();
		labelStyleTitle.font = skin.getFont("big");
		labelStyleTitle.fontColor = new Color(0f, 0.165f, 0.322f, 1);
		skin.add("title", labelStyleTitle);
		
		CheckBoxStyle checkBoxStyle = new CheckBoxStyle();
		checkBoxStyle.checkboxOn = skin.getDrawable("livesaver_off");
		checkBoxStyle.checkboxOff = skin.getDrawable("livesaver_on");
		checkBoxStyle.font = skin.getFont("default");
		skin.add("default", checkBoxStyle);
		
		SliderStyle sliderStyle = new SliderStyle();
		sliderStyle.knob = skin.getDrawable("slider_pointer");
		sliderStyle.background = skin.getDrawable("slider_rail");
		skin.add("default-horizontal", sliderStyle);
		
		ButtonStyle shipButtonStyle = new ButtonStyle();
		shipButtonStyle.up = skin.getDrawable("ship_button_up");
		shipButtonStyle.down = skin.getDrawable("ship_button_down");
		skin.add("ship-button", shipButtonStyle);
		
		return skin;
	}

    public class AbstractScrollPane extends ScrollPane {
        ShapeRenderer shapeRenderer = new ShapeRenderer();

        public AbstractScrollPane(Actor actor){
            super(actor);
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
}

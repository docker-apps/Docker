package com.docker.domain.gameobject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.docker.Docker;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Ship extends Actor {
	private int gridWidth;
	private int gridHeight;
	private int breakThreshold;
	private int capsizeThreshold;
	private List<Container> containers;
	private Container previewContainer;
	private Integer gridSize;
	private int[] topLine;
	private float[][] grid;
	private float yGridstart;
	private float xGridstart;
	private float[] breakValues;
	private boolean isBreaking = false;
	public boolean isTakingOff;
	private boolean isStaticAnimationRunning = false;
	private boolean isSunken = false;
	private int breakPos;

	private Sound containerSound;

	private TextureRegion bodyLeft;
	private TextureRegion bodyCenter;
	private TextureRegion bodyRight;
	private TextureRegion tower;
	private TextureRegion mast;
	private TextureRegion indicatorLampOn;
	private TextureRegion bodyBrokenLeft;
	private TextureRegion bodyBrokenRight;
	private FrameBuffer fbo;
	private ShaderProgram snapshotShader;

    private Boolean playContainerSound = true;

    public Ship(int gridWidth, int gridHeight, int capsizeThreshold, int breakThreshold, float x, float y) {
		super();

		if(gridWidth <= 1)
			throw new IllegalArgumentException();	

		this.setX(x);
		this.setY(y);
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.capsizeThreshold = capsizeThreshold;
		this.breakThreshold = breakThreshold;
		this.containers = new ArrayList<Container>();
		this.setBreakValues(null);

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("img/docker.atlas"));
		this.bodyLeft = atlas.findRegion("ship_body_left");
		this.bodyCenter = atlas.findRegion("ship_body_center");
		this.bodyRight = atlas.findRegion("ship_body_right");
		this.tower = atlas.findRegion("ship_tower");
		this.mast = atlas.findRegion("ship_mast");
		this.indicatorLampOn = atlas.findRegion("indicator_lamp_on");
		this.bodyBrokenLeft = atlas.findRegion("ship_body_broken_left");
		this.bodyBrokenRight = atlas.findRegion("ship_body_broken_right");

		this.gridSize = this.bodyCenter.getRegionWidth();

		this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());

		this.xGridstart = this.getX()+bodyLeft.getRegionWidth() - gridSize;
		this.yGridstart = this.getY()+bodyCenter.getRegionHeight();

		containerSound = Gdx.audio.newSound(Gdx.files.internal("container_load.wav"));
		createTopLineAndGrid();
	}

	/**
	 * Adds the Container if possible at the given X position
	 * if returned false, it was not possible to set the Container
	 * 
	 * @param x
	 * @param container
	 * @return
	 */
	public boolean addContainer(float x, Container container){
		Vector2 gridCoords = getGridCoords(x, container.getLength());
		if(gridCoords.y >= 0 &&  gridCoords.y < gridHeight){
			Vector2 realCoords = getRealCoord(gridCoords);
			container.setPosition(realCoords.x, realCoords.y);
			this.containers.add(container);
            if (playContainerSound) {
                containerSound.play();
            }
            createTopLineAndGrid();
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Sets a Preview Container, where the Container possible could be set.
	 * 
	 * @param x
	 * @param container
	 */
	public void setPreviewContainer(float x, Container container){
		Vector2 gridCoords = getGridCoords(x, container.getLength());
		if(gridCoords.y >= 0 ){
			previewContainer = new Container(container);
			previewContainer.setColor(new Color(1f, 0f, 0f, 0.5f));
			if (gridCoords.y < gridHeight) {
				previewContainer.setColor(new Color(1f, 1f, 1f, 0.5f));
			}
			Vector2 realCoords = getRealCoord(gridCoords);
			previewContainer.setPosition(realCoords.x, realCoords.y);
		}else{
			previewContainer = null;
		}
	}

	/**
	 * 
	 * @param x
	 * @param containerLenght
	 * @return
	 */
	private int getXGrid(float x, int containerLenght) {
		int xGrid = (int) Math.floor((double) (x-xGridstart)/gridSize); 
        if(xGrid < 0){
        	xGrid = 0;
        }else{
        	float noSpace = this.gridWidth- (xGrid + containerLenght);
        	if (noSpace < 0 ) {
				xGrid = this.gridWidth-containerLenght;
			}
        }
		return xGrid;
	}


	/**
	 * 
	 * @param x
	 * @param containerLenght
	 * @return
	 */
	private Vector2 getGridCoords(float x, int containerLenght){
		Vector2 gridCoords = new Vector2();
		gridCoords.x = getXGrid(x, containerLenght);
		gridCoords.y = getYGrid((int) gridCoords.x, containerLenght);
		return gridCoords; 
	}


	/**
	 * Returns Coordinates where the Container will be places
	 * If it returns null the Container can't be placed at this place.
	 * 
	 * @param x
	 * @param container
	 * @return 
	 */
	public Vector2 getRealCoord(float x, Container container){
		Vector2 gridCoords = getGridCoords(x, container.getLength());
		Vector2 realCoords = null;
		if (gridCoords.y < gridHeight) {
			realCoords = getRealCoord(gridCoords);
		}
		return realCoords;
	}


	/**
	 * 
	 * @param gridCoords
	 * @return
	 */
	private Vector2 getRealCoord(Vector2 gridCoords){
		Vector2 realCoords = new Vector2();
		realCoords.x = gridCoords.x * gridSize + xGridstart;
		realCoords.y = gridCoords.y * gridSize + yGridstart;
		return realCoords;
	}

	/**
	 * Creates a TextureRegion with the current image data of the whole ship.
	 * 
	 * Do not call this in the draw() method.
	 * 
	 * @return
	 */
	private TextureRegion takeSnapshot(){
		// init framebuffer and shader (needed to draw alpha correctly in the framebuffer
		if(this.fbo == null){
			fbo = new FrameBuffer(Format.RGBA8888, (int)getStage().getWidth(), (int)getStage().getHeight(), false);
			fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
		if(this.snapshotShader == null){
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
			if (snapshotShader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + snapshotShader.getLog());

		}

		fbo.begin();
		Batch batch = this.getStage().getBatch();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f); //transparent black
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //clear the color buffer
		batch.setShader(snapshotShader);
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.enableBlending();
		batch.begin();
		this.draw(batch, 1f);
		//reset batch
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.end();
		snapshotShader.end();
		fbo.end();

		TextureRegion fboRegion = new TextureRegion(fbo.getColorBufferTexture());
		fboRegion.flip(false, true);
		return fboRegion;
	}

	private void removeFromStage(){
		this.clearActions();
		this.clearListeners();
		getStage().getRoot().removeActor(this);
	}

	/**
	 * Makes the ship sail away to the left, out of the screen.
	 * 
	 * Initiates an animation and sets isTakingOff = true
	 * 
	 */
	public void takeOff(){
		this.previewContainer = null;
		this.isTakingOff = true;
		TextureRegion snapshot = this.takeSnapshot();

		Image img = new Image(snapshot);

		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(img.getWidth()*-1, 0f);
		moveAction.setDuration(5);
		moveAction.setInterpolation(Interpolation.pow2In);

		Action completeAction = new Action(){
			public boolean act( float delta ) {
				removeFromStage();
				return true;
			}
		};

		// chain the two actions and add it to this actor
		SequenceAction actions = new SequenceAction(moveAction, completeAction);
		img.addAction(actions);
		this.getStage().addActor(img);
		this.isStaticAnimationRunning = true;
	}


	/**
	 * Capsizes the ship. The direction is dependent on the capsizeValue.
	 * 
	 * Initiates an animation.
	 * 
	 * @param capsizeValue
	 */
	public void capsize(float capsizeValue){
		TextureRegion region = takeSnapshot();

		this.startCapsizeAnimation(this, capsizeValue);

		Image img = new Image(region);
		startCapsizeAnimation(img, capsizeValue);
		this.getStage().addActor(img);
	}
	
	
	public static Ship getRandomShip() {
		int width = 5 + (int)(Math.random() * ((10 - 5) + 1));
		 Ship ship = new Ship(width, 5, 5, 5, 0f, 0f);
		 ship.setPosition((Docker.WIDTH-ship.getWidth())/2-20f, 10f);
		return ship;
	}

	/**
	 * Add a capsizing animation in form of an Action to the specified actor.
	 * 
	 * Can be used for the capsizing animation (of course) as well as for the breaking animation
	 * 
	 * @param actor The actor to which should be animated.
	 * @param capsizeValue if positive, the ship will capsize to the left, if negative, to the right.
	 */
	private void startCapsizeAnimation(Actor actor, float capsizeValue){
		this.setStaticAnimationRunning(true);
		
		float duration = 6;
		
		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(actor.getX(), actor.getY()-this.getWidth());
		moveAction.setDuration(duration*0.75f);
		moveAction.setInterpolation(Interpolation.exp5In);
		actor.addAction(moveAction);
		
		actor.setOrigin(actor.getWidth()/2-actor.getWidth()/6*Math.signum(capsizeValue), 10);
		RotateByAction rotateAction = new RotateByAction();
		rotateAction.setAmount(90*Math.signum(capsizeValue));
		rotateAction.setDuration(duration);
		rotateAction.setInterpolation(Interpolation.exp5Out);
		
		ParallelAction sinkingAction = new ParallelAction(moveAction, rotateAction);
		
		// set the sunken flag
		Action completeAction = new Action(){
			public boolean act( float delta ) {
				setSunken(true);
				return true;
			};
		};
		
		SequenceAction actions = new SequenceAction(sinkingAction, completeAction);

		actor.addAction(actions);
	}


	/**
	 * Breaks the ship in half, at the specified position.
	 * 
	 * Initiates an animation and sets isBreaking = true
	 * 
	 * @param breakPosition The (x-Grid) position, at which the ship should break
	 */
	public void breakShip(int breakPosition){
		float breakingXPos = this.xGridstart + this.gridSize*breakPosition;
		this.isBreaking = true;
		this.previewContainer = null;
		TextureRegion fboRegion1 = takeSnapshot();
		TextureRegion fboRegion2 = takeSnapshot();

		//left part
		fboRegion1.setRegionWidth((int)breakingXPos);
		Image img = new Image(fboRegion1);
		img.setOrigin(breakingXPos, this.getY());
		startCapsizeAnimation(img, -1);
		this.getStage().addActor(img);

		//right part
		fboRegion2.setRegionX((int)breakingXPos);
		fboRegion2.setRegionWidth(fbo.getWidth() - (int)breakingXPos);
		Image img2 = new Image(fboRegion2);
		img2.setPosition(breakingXPos, 0);
		img2.setOrigin(breakingXPos, this.getY());
		startCapsizeAnimation(img2, 1);
		this.getStage().addActor(img2);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		this.xGridstart = this.getX()+bodyLeft.getRegionWidth() - gridSize;
		this.yGridstart = this.getY()+bodyCenter.getRegionHeight();
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		if(isStaticAnimationRunning == false){
			// draw ship
			batch.draw(this.bodyLeft, this.getX(), this.getY());
			batch.draw(this.mast, this.getX()+this.bodyLeft.getRegionWidth()-this.getElementWidth()-this.mast.getRegionWidth()-1, this.getY()+this.bodyLeft.getRegionHeight());
			for (int i = 0; i < this.gridWidth-2; i++) {
				float xPos = this.getX()+this.bodyLeft.getRegionWidth()+(getElementWidth()*i);
				if(isBreaking && i == this.breakPos+1){
					batch.draw(this.bodyBrokenLeft, xPos, this.getY());
				}
				else if(isBreaking && i == this.breakPos+2){
					batch.draw(this.bodyBrokenRight, xPos, this.getY());
				}
				else{
					batch.draw(this.bodyCenter, xPos, this.getY());
				}
			}
			float bodyRightX = this.getX()+this.bodyLeft.getRegionWidth()+(getElementWidth()*(this.gridWidth-2));
			batch.draw(this.bodyRight, bodyRightX, this.getY());
			batch.draw(this.tower, bodyRightX+this.bodyRight.getRegionWidth()-this.tower.getRegionWidth()-1, this.getY()+this.bodyRight.getRegionHeight());

			// draw preview Container
			if (previewContainer != null) {
				previewContainer.draw(batch, parentAlpha);
			}

			// draw Containers
			for (Container container : containers) {
				container.draw(batch, parentAlpha);
			}

			float lampsOffsetX = this.getX() + this.bodyLeft.getRegionWidth() - 
					this.getElementWidth()/2-this.indicatorLampOn.getRegionWidth()/2;
			float lampsOffsetY = this.getY()+34;
			// draw indicator lamps
			if(this.breakValues != null){
				for (int i = 0; i < this.breakValues.length; i++) {
					if(breakValues[i] <= 0.5)
						batch.setColor(Color.WHITE);
					else if(breakValues[i] <= 1)
						batch.setColor(1, 1, 0, 1);
					else
						batch.setColor(1f, 0, 0, 1);
					batch.draw(
							this.indicatorLampOn,
							lampsOffsetX + this.getElementWidth()*i,
							lampsOffsetY);
				}
				batch.setColor(Color.WHITE);
			}
		}	
	}

	@Override
	public float getWidth(){
		return 
				this.bodyLeft.getRegionWidth() + 
				this.bodyCenter.getRegionWidth()*(this.gridWidth-2) + 
				this.bodyRight.getRegionWidth();
	}

	public float getElementWidth(){
		return this.bodyCenter.getRegionWidth();
	}

	/**
	 * Returns on with GridY the Container fits, 
	 * if result is negativ, it's not possible to Fit it in this GridX
	 * @param gridX
	 * @param lenght
	 * @return 
	 */
	public int getYGrid(int gridX, int lenght){
		if(lenght == 1){
			return (topLine[gridX]);
		}
		int topline = getYGrid(gridX + 1, lenght -1 );
		if (topLine[gridX] > topline) {
			return (topLine[gridX]);
		}
		return topline;
	}

	public void createTopLineAndGrid(){
		this.topLine = new int[gridWidth];
		this.grid = new float[gridWidth][gridHeight];
		for (Container container : containers) {
			int gridX = (int) (container.getX()-xGridstart)/gridSize;
			int gridY = (int) ((container.getY()-yGridstart)/gridSize);
			int lenght = container.getLength();
			float weightPerLenght = (float)container.getWeight()/(float)lenght;
			while (lenght > 0) {
				grid[gridX][gridY] = weightPerLenght;
				if (topLine[gridX] < gridY+1) {
					topLine[gridX] = gridY+1;
				}
				gridX++;
				lenght--;
			}
		}
	} 

	public float[][] getGrid(){
		return this.grid;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public int getBreakThreshold() {
		return breakThreshold;
	}

	public void setBreakThreshold(int breakThreshold) {
		this.breakThreshold = breakThreshold;
	}

	public int getCapsizeThreshold() {
		return capsizeThreshold;
	}

	public void setCapsizeThreshold(int capsizeThreshold) {
		this.capsizeThreshold = capsizeThreshold;
	}

	public List<Container> getContainers() {
		return containers;
	}

	public void setContainers(List<Container> containers) {
		this.containers = containers;
	}

	public void setBreakValues(float[] breakValues) {
		this.breakValues = breakValues;
	}

	public boolean isBreaking() {
		return isBreaking;
	}

	public boolean isTakingOff() {
		return isTakingOff;
	}

	public boolean isStaticAnimationRunning() {
		return isStaticAnimationRunning;
	}
	
	public void setStaticAnimationRunning(boolean isStaticAnimationRunning){
		this.isStaticAnimationRunning = isStaticAnimationRunning;
	}

    public void playContainerSound(Boolean playSound) {
        this.playContainerSound = playSound;
    }

	public boolean isSunken() {
		return isSunken;
	}

	public void setSunken(boolean isSunken) {
		this.isSunken = isSunken;
	}

}

package com.docker.domain.gameobject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.docker.Docker;
import com.docker.domain.gameobject.shipskins.IShipSkin;
import com.docker.technicalservices.Persistence;
import com.docker.technicalservices.Resource;
import com.docker.technicalservices.SoundHandler;

public class Ship extends Actor {	
	private static final Color PREVIEW_CONTAINER_INVALID_COLOR = new Color(1f, 1f, 1f, 0.5f);
	private static final Color PREVIEW_CONTAINER_COLOR = new Color(1f, 0f, 0f, 0.5f);
	
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
	private boolean isTakingOff;
	private boolean isStaticAnimationRunning = false;
	private boolean isSunken = false;
	private int breakPos;

	private IShipSkin skin;
	private FrameBuffer fbo;
	private Pixmap gridBoundsPixmap;
	private Texture gridBoundsTexture;

	private float gridBoundsAlpha = 0f;
	private float gridBoundsDecay = 1f;

	private final Pool<SmokePuff> smokePool= new Pool<Ship.SmokePuff>(5, 10) {

		@Override
		protected SmokePuff newObject() {
			return new SmokePuff();
		}
	};

	private List<SmokePuff> smokePuffs = new ArrayList<Ship.SmokePuff>();
	
	public Ship(int gridWidth, int gridHeight, int capsizeThreshold, int breakThreshold, float x, float y, IShipSkin skin) {
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

		this.skin = skin;

		this.gridSize = skin.getBodyCenter().getRegionWidth();

		this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());

		this.xGridstart = this.getX()+skin.getBodyLeft().getRegionWidth() - gridSize;
		this.yGridstart = this.getY()+skin.getBodyCenter().getRegionHeight();

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
			Random rand = new Random();

			//create a smoke puff, but only if containerpart actually touches anything
			for (int i = 0; i < container.getLength(); i++) {
				if((int)gridCoords.y == 0 || this.topLine[(int) gridCoords.x + i] == ((int)gridCoords.y)){
					for (int j = 0; j < 3; j++) {
						SmokePuff puff = smokePool.obtain();
						this.smokePuffs.add(puff);
						float elementOffset = container.getX()+i*container.getElementWidth();
						puff.init(
								elementOffset + j*container.getElementWidth()/3 + rand.nextFloat()*container.getElementWidth()/3, 
								container.getY(), rand);
						this.getStage().addActor(puff);
					}					
				}
			}

			this.containers.add(container);

			SoundHandler.playSound(Resource.getContainerSound());
			
			Integer totalContainer = (Integer) Persistence.getStatisticsMap().get("totalContainer");
			Persistence.saveStatisticValue("totalContainer", totalContainer+1);
			Integer totalWeight = (Integer) Persistence.getStatisticsMap().get("totalWeight");
			Persistence.saveStatisticValue("totalWeight", totalWeight + container.getWeight());
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
			previewContainer.setColor(PREVIEW_CONTAINER_COLOR);
			if (gridCoords.y < gridHeight) {
				previewContainer.setColor(PREVIEW_CONTAINER_INVALID_COLOR);
			}
			if(gridCoords.y >= gridHeight-1 || x < xGridstart - 20f || x > xGridstart + gridSize * gridWidth)
				this.gridBoundsAlpha = 1f;
			Vector2 realCoords = getRealCoord(gridCoords);
			previewContainer.setPosition(realCoords.x, realCoords.y);
		}else{
			previewContainer = null;
		}
	}

	public void clearPreviewContainer(){
		this.previewContainer = null;
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

		fbo.begin();
		Batch batch = this.getStage().getBatch();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f); //transparent black
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //clear the color buffer
		batch.setShader(Resource.getSnapshotShader());
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.enableBlending();
		batch.begin();
		this.draw(batch, 1f);
		batch.end();
		Resource.getSnapshotShader().end();
		fbo.end();

		TextureRegion fboRegion = new TextureRegion(fbo.getColorBufferTexture());
		fboRegion.flip(false, true);
		return fboRegion;
	}

	private void disposeFbo(){
		if(fbo != null){
			fbo.dispose();
			fbo = null;                	
		}
	}

	private void removeFromStage(){
		this.disposeFbo();
		this.disposeGridBounds();
		this.clearActions();
		this.clearListeners();
		getStage().getRoot().removeActor(this);
	}

	public void runIn(){
		//TODO: This and TakeOff is very similar. Should be refactored if possible.
		this.previewContainer = null;
		this.isTakingOff = true;
		final TextureRegion snapshot = this.takeSnapshot();

		final Image img = new Image(snapshot);
		img.setPosition(img.getWidth(), 0f);

		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(0f, 0f);
		moveAction.setDuration(5);
		moveAction.setInterpolation(Interpolation.pow2);

		Action completeAction = new Action(){
			public boolean act( float delta ) {
				isTakingOff = false;
				isStaticAnimationRunning = false;
				img.remove();
				snapshot.getTexture().dispose();
				disposeFbo();
				return true;
			}
		};
		
		Action waterSplashAction = new Action() {
			Random rand = new Random();

			@Override
			public boolean act(float delta) {
				float waterHeight = 20f;
				float offset = getX();
				
				if(rand.nextFloat() > 0.5){
					float randomOffset = rand.nextFloat()*getWidth();
					WaterSplash splashTest = new WaterSplash(
							actor.getX()+ offset + randomOffset,
							waterHeight);
					getStage().addActor(splashTest);
				}
				return true;
			}
		};
		ParallelAction parallelAction = new ParallelAction(moveAction, waterSplashAction);

		// chain the two actions and add it to this actor
		SequenceAction actions = new SequenceAction(parallelAction, completeAction);
		img.addAction(actions);
		this.getStage().addActor(img);
		this.isStaticAnimationRunning = true;
		
		SoundHandler.playSound(Resource.getShipRunInSound());
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
		final TextureRegion snapshot = this.takeSnapshot();

		final Image img = new Image(snapshot);

		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(img.getWidth()*-1, 0f);
		moveAction.setDuration(5);
		moveAction.setInterpolation(Interpolation.pow2In);

		Action completeAction = new Action(){
			public boolean act( float delta ) {
				removeFromStage();
				isTakingOff = false;
				isStaticAnimationRunning = false;
				setVisible(false);
				img.remove();
				snapshot.getTexture().dispose();
				disposeFbo();
				return true;
			}
		};
		
		Action waterSplashAction = new Action() {
			Random rand = new Random();

			@Override
			public boolean act(float delta) {
				float waterHeight = 20f;
				float offset = getX();
				
				if(rand.nextFloat() > 0.5){
					float randomOffset = rand.nextFloat()*getWidth();
					WaterSplash splashTest = new WaterSplash(
							actor.getX()+ offset + randomOffset,
							waterHeight);
					getStage().addActor(splashTest);
				}
				return true;
			}
		};
		
		ParallelAction parallelAction = new ParallelAction(moveAction, waterSplashAction);

		// chain the two actions and add it to this actor
		SequenceAction actions = new SequenceAction(parallelAction, completeAction);
		img.addAction(actions);
		this.getStage().addActor(img);
		this.isStaticAnimationRunning = true;
		
		SoundHandler.playSound(Resource.getShipTakeOffSound());
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

		//this.startCapsizeAnimation(this, capsizeValue);

		Image img = new Image(region);
		startCapsizeAnimation(img, capsizeValue, getX()+getWidth()/2, yGridstart);
		this.getStage().addActor(img);
	}


	public static Ship getRandomShip(IShipSkin skin) {
		int width = 5 + (int)(Math.random() * ((10 - 5) + 1));
		Ship ship = new Ship(width, 5, 5, 5, 0f, 0f, skin);
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
	private void startCapsizeAnimation(Actor actor, final float capsizeValue, final float originX, final float originY){
		this.setStaticAnimationRunning(true);

		float duration = 10;

		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(actor.getX(), actor.getY()-this.getWidth());
		moveAction.setDuration(duration*0.75f);
		moveAction.setInterpolation(Interpolation.exp5In);

		actor.setOrigin(originX, originY);
		RotateByAction rotateAction = new RotateByAction();
		rotateAction.setAmount(90*0.75f*Math.signum(capsizeValue));
		rotateAction.setDuration(duration);
		rotateAction.setInterpolation(Interpolation.exp5Out);

		Action waterSplashAction = new Action() {
			Random rand = new Random();

			@Override
			public boolean act(float delta) {
				if(rand.nextFloat() > 0.8){
					float waterHeight = 20f;
					Vector2 p1 = new Vector2(	// upper left corner of ship body, relative to origin
							getX() - originX, 
							getY() + yGridstart - originY - 10f);
					Vector2 p2 = new Vector2(	// lower left corner of ship body, relative to origin
							getX() - originX, 
							getY() - originY);
					Vector2 p3 = new Vector2(	// lower right corner of ship body, relative to origin
							getX()+getWidth() - originX, 
							getY() - originY);

					//rotate points (around origin)
					p1.rotate(actor.getRotation());
					p2.rotate(actor.getRotation());
					p3.rotate(actor.getRotation());
					
					//translate points back relative to screen coordinates
					p1.add(actor.getX()+originX, actor.getY()+originY);
					p2.add(actor.getX()+originX, actor.getY()+originY);
					p3.add(actor.getX()+originX, actor.getY()+originY);
					
					//calculate left and right bounds of the area where the ship and the water collides
					float xBoundLeft = (float) Math.max(
							p1.x + Math.tan(Math.toRadians(90-actor.getRotation())) * (waterHeight-p1.y),
							p1.x
							);
					float xBoundRight = (float) Math.min(
							p2.x + Math.tan(Math.toRadians(90-actor.getRotation())) * (waterHeight-p2.y),
							p3.x
							);
					
					// create new splash
					Random rand = new Random();
					float randomOffset = rand.nextFloat()*(xBoundRight-xBoundLeft);
					WaterSplash splash = new WaterSplash(xBoundLeft + randomOffset, waterHeight);
					getStage().addActor(splash);
				}
				return true;
			}
		};

		ParallelAction sinkingAction = new ParallelAction(moveAction, rotateAction, waterSplashAction);

		// set the sunken flag
		Action completeAction = new Action(){
			public boolean act( float delta ) {
				setSunken(true);
				isStaticAnimationRunning = false;
				setVisible(false);
				actor.remove();
				if(actor instanceof Image){
					Drawable drawable = ((Image) actor).getDrawable();   
					if(drawable instanceof TextureRegionDrawable){
						((TextureRegionDrawable) drawable).getRegion().getTexture().dispose();
					}
				}
				disposeFbo();
				return true;
			}
		};

		SequenceAction actions = new SequenceAction(sinkingAction, completeAction);

		actor.addAction(actions);
		
		SoundHandler.playSound(Resource.getBubbleSound());
	}


	/**
	 * Breaks the ship in half, at the specified position.
	 * Initiates an animation and sets isBreaking = true
	 * 
	 * @param breakPosition The (x-Grid) position, at which the ship should break
	 */
	public void breakShip(int breakPosition){
		this.breakPos = breakPosition;
		float breakingXPos = this.xGridstart + this.gridSize*(breakPosition);
		this.isBreaking = true;
		this.previewContainer = null;
		TextureRegion fboRegion1 = takeSnapshot();
		TextureRegion fboRegion2 = takeSnapshot();

		//left part
		fboRegion1.setRegionWidth((int)breakingXPos);
		Image img = new Image(fboRegion1);
		img.setOrigin(breakingXPos, this.getY());
		startCapsizeAnimation(img, -1, img.getWidth()/2, yGridstart);
		this.getStage().addActor(img);

		//right part
		fboRegion2.setRegionX((int)breakingXPos);
		fboRegion2.setRegionWidth(fbo.getWidth() - (int)breakingXPos);
		Image img2 = new Image(fboRegion2);
		img2.setPosition(breakingXPos, 0);
		img2.setOrigin(breakingXPos, this.getY());
		startCapsizeAnimation(img2, 1, img2.getWidth()/2,yGridstart);
		this.getStage().addActor(img2);
		
		SoundHandler.playSound(Resource.getShipBreakSound());
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		this.xGridstart = this.getX()+skin.getBodyLeft().getRegionWidth() - gridSize;
		this.yGridstart = this.getY()+skin.getBodyCenter().getRegionHeight();

		this.gridBoundsAlpha = Math.max(0f, this.gridBoundsAlpha - this.gridBoundsDecay * delta);

		for (int i = 0; i < smokePuffs.size(); i++) {
			SmokePuff puff = smokePuffs.get(i);
			if(puff.isDead())
			{
				puff.remove();
				smokePool.free(puff);
				smokePuffs.remove(puff);
			}
		}
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		if(!isStaticAnimationRunning){
			// draw ship
			batch.draw(
					skin.getBodyLeft(), 
					getX(), 
					getY());
			batch.draw(
					skin.getMast(), 
					getX()+skin.getMastOffset().x, 
					getY()+skin.getBodyLeft().getRegionHeight()+skin.getMastOffset().y);
			for (int i = 0; i < this.gridWidth-2; i++) {
				float xPos = this.getX()+this.skin.getBodyLeft().getRegionWidth()+(getElementWidth()*i);
				if(isBreaking && i == this.breakPos-2){
					batch.draw(
							skin.getBodyBrokenLeft(),
							xPos, 
							getY());
				}
				else if(isBreaking && i == this.breakPos-1){
					batch.draw(
							skin.getBodyBrokenRight(),
							xPos,
							getY());
				}
				else{
					batch.draw(
							skin.getBodyCenter(),
							xPos,
							this.getY());
				}
			}
			float bodyRightX = getX()+skin.getBodyLeft().getRegionWidth()+(getElementWidth()*(gridWidth-2));
			batch.draw(
					skin.getBodyRight(), 
					bodyRightX, 
					this.getY());
			batch.draw(
					skin.getTower(),
					bodyRightX+this.skin.getBodyRight().getRegionWidth()-skin.getTower().getRegionWidth()+skin.getTowerOffset().x, 
					this.getY()+skin.getBodyRight().getRegionHeight()+skin.getTowerOffset().y);

			// draw preview Container
			if (previewContainer != null) {
				previewContainer.draw(batch, parentAlpha);
			}

			if(this.gridBoundsAlpha > 0f)
				batch.draw(
						this.getGridBoundsTexture(), 
						xGridstart, 
						yGridstart);

			// draw Containers
			for (Container container : containers) {
				container.draw(batch, parentAlpha);
			}

			float lampsOffsetX = getX() + skin.getBodyLeft().getRegionWidth() - 
					getElementWidth()/2-skin.getIndicatorLamp().getRegionWidth()/2;
			float lampsOffsetY = this.getY()+34;
			// draw indicator lamps
			if(this.breakValues != null){
				for (int i = 0; i < this.breakValues.length; i++) {
					if(breakValues[i] <= 0.5)
						batch.setColor(Color.WHITE);
					else if(breakValues[i] < 1)
						batch.setColor(1, 1, 0, 1);
					else
						batch.setColor(1f, 0, 0, 1);
					batch.draw(
							skin.getIndicatorLamp(),
							lampsOffsetX + getElementWidth()*i,
							lampsOffsetY);
				}
				batch.setColor(Color.WHITE);
			}
		}	
	}

	@Override
	public float getWidth(){
		return 
				skin.getBodyLeft().getRegionWidth() + 
				skin.getBodyCenter().getRegionWidth()*(this.gridWidth-2) + 
				skin.getBodyRight().getRegionWidth();
	}
	
	@Override
	public float getHeight(){
		return (float) Math.max(
				skin.getBodyLeft().getRegionHeight() + skin.getMastOffset().y + skin.getMast().getRegionHeight(), 
				skin.getBodyRight().getRegionHeight() + skin.getTowerOffset().y + skin.getTower().getRegionHeight());
	}

	public float getElementWidth(){
		return skin.getBodyCenter().getRegionWidth();
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

	/**
	 * @return the texture to display the grid's dimensions.
	 */
	protected Texture getGridBoundsTexture(){
		disposeGridBounds();
		int width = gridWidth * gridSize;
		int height = gridHeight * gridSize;
		this.gridBoundsPixmap = new Pixmap(width, height, Format.RGBA8888);

		this.gridBoundsPixmap.setColor(1f, .5f, .1f, this.gridBoundsAlpha);
		this.gridBoundsPixmap.drawRectangle(0, 0, width, height);

		this.gridBoundsTexture = new Texture(gridBoundsPixmap);

		return this.gridBoundsTexture;
	}

	protected void disposeGridBounds(){
		if (this.gridBoundsPixmap != null){
			this.gridBoundsPixmap.dispose();
			this.gridBoundsPixmap = null;
		}
		if (this.gridBoundsTexture != null){
			this.gridBoundsTexture.dispose();
			this.gridBoundsTexture = null;
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

	public boolean isSunken() {
		return isSunken;
	}

	public void setSunken(boolean isSunken) {
		this.isSunken = isSunken;
	}

	private class SmokePuff extends Actor implements Poolable{
		private final static float FADE_OUT_SPEED = 2;
		private final static float RISE_SPEED = 10;
		private Texture texture;
		private float alpha;

		public SmokePuff(){
			super();
			this.texture = Resource.getSmokePuffTexture();
		}

		public void init(float x, float y, Random rand){
			float size= texture.getWidth()/2 + (texture.getWidth()/2)*rand.nextFloat();
			this.setSize(size, size);
			this.setPosition(x, y);
			this.alpha = 1;

		}

		public boolean isDead(){
			return this.alpha <= 0;
		}

		@Override
		public void act(float delta){
			this.setScale(this.getScaleX()+1f*delta);
			this.setColor(1f, 1f, 1f, this.alpha);
			this.alpha-=delta*FADE_OUT_SPEED;
			this.setY(this.getY()+RISE_SPEED*delta);
		}

		@Override
		public void draw(Batch batch, float delta){
			float drawWidth = getWidth()*getScaleX();
			float drawHeight = getHeight()*getScaleY();
			batch.setColor(this.getColor());
			batch.draw(texture, getX()-drawWidth/2, getY()-drawHeight/2, drawWidth, drawHeight);
			batch.setColor(Color.WHITE);
		}

		@Override
		public void reset() {
			this.setScale(1);
			this.setColor(1f, 1f, 1f, 1f);
		}

	}

	private class WaterSplash extends Actor{
		private Array<AtlasRegion> frames;
		private Animation animation;
		private float stateTime;

		public WaterSplash(float x, float y){
			this.setX(x);
			this.setY(y);

			frames = Resource.findRegions("water_splash");
			animation = new Animation(0.2f, frames);
		}

		@Override
		public void act(float delta){
			stateTime+=delta;
			if (isFinished()) {
				remove();
			}
		}

		@Override
		public void draw(Batch batch, float parentAlpha){
			TextureRegion frame = animation.getKeyFrame(stateTime);
			batch.draw(frame, getX()-frame.getRegionWidth()/2, getY());
		}

		public boolean isFinished(){
			return animation.isAnimationFinished(stateTime);
		}
	}

}

package com.docker.domain.game;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.docker.Docker;
import com.docker.domain.gameobject.Container;
import com.docker.domain.gameobject.Ship;
import com.docker.domain.gameobject.Train;
import com.docker.domain.gameobject.shipskins.ShipSkinManager;
import com.docker.technicalservices.Resource;

public class TutorialGame extends AbstractGame {

	private final static int NO_ARROW = 0;
	private final static int ARROW_1 = 1;
	private final static int ARROW_2 = 2;
	private final static int ARROW_3 = 3;

	private final static float FINGER_INIT_X = 10;
	private final static float FINGER_INIT_Y = -30;

	private Pixmap dimPixMap;
	private Texture dimTexture;
	private Actor dimImage;
	private Group group;
	private Actor textLabel;

	public TutorialGame(Docker application) {
		super(application);

		LinkedList<Container> containers = new LinkedList<Container>();
		containers.add(new Container(2, 4));
		containers.add(new Container(4, 3));
		containers.add(new Container(3, 1));
		containers.add(new Container(1, 2));
		containers.add(new Container(3, 4));
		containers.add(new Container(2, 1));
		containers.add(new Container(2, 5));
		this.setTrain(new Train(containers, 5, 0f, getStage().getHeight()-23));
		this.setShip(new Ship(10, 5, 18, 2, 0f, 0f, ShipSkinManager.getSkin(ShipSkinManager.DEFAULT_SHIP_SKIN)));
		this.ship.setPosition(getStage().getWidth()/2-ship.getWidth()/2-20f, 10f);
		this.setLoadRating(new LoadRating(this.ship.getBreakThreshold(), this.ship.getCapsizeThreshold(), 1));
		this.setLives(3);

		dimPixMap = null;

		//set foreground as a normal actor.
		Actor foreground = this.getStage().getForeground();
		this.getStage().setForeground(null);
		this.getStage().addActor(foreground);


		group = new Group();
		dimTexture = null;

		this.displayTutorial();
	}

	private void displayTutorial(){
		SequenceAction tutorialSequence = new SequenceAction();

		tutorialSequence.addAction(getBaseAction("Welcome to Docker!\nThis is your ship", 3f, ARROW_1));
		tutorialSequence.addAction(getBaseAction("The train delivers containers", 3f, ARROW_2));
		tutorialSequence.addAction(getBaseAction("Position them on your ship\nby dragging your finger...", 4f, NO_ARROW, true));
		tutorialSequence.addAction(getBaseAction("..and letting go to deploy them", 3f, NO_ARROW, true));
		tutorialSequence.addAction(getBaseAction("If a container reaches the \nscreen border, you'll lose a life", 3f));
		tutorialSequence.addAction(getBaseAction("The containers numbers \nindicate their total weight.", 3f));
		tutorialSequence.addAction(getBaseAction("Sacrifice unwanted containers\nby flinging them to the right", 3f));
		tutorialSequence.addAction(getBaseAction("This meter shows you the\nbalance of the ship", 3f, ARROW_3));
		tutorialSequence.addAction(getBaseAction("The ship's lights \nindicate the weight distribution", 3f));
		tutorialSequence.addAction(getBaseAction("Try to evenly distribute \nthe weight", 3f));
		tutorialSequence.addAction(getBaseAction("Have fun!", 1f));
		tutorialSequence.addAction(getEndGameAction());

		this.group.addAction(tutorialSequence);
		this.getStage().addActor(group);
	}


	/**
	 * @param text the text to be displayed.
	 * @param duration how long the slide will be displayed (excluding the fade in/out animation)
	 * @return
	 */
	private Action getBaseAction(final String text, float duration){
		return getBaseAction(text, duration, NO_ARROW, false);
	}

	/**
	 * @param text the text to be displayed.
	 * @param duration how long the slide will be displayed (excluding the fade in/out animation)
	 * @param arrow the arrow to be shown (use the constants in this class)
	 * @return
	 */
	private Action getBaseAction(final String text, float duration, final int arrow){
		return getBaseAction(text, duration, arrow, false);
	}

	/**
	 * @param text the text to be displayed.
	 * @param duration how long the slide will be displayed (excluding the fade in/out animation)
	 * @param arrow the arrow to be shown (use the constants in this class)
	 * @param displayfinger wether to display the finger animation
	 * @return
	 */
	private Action getBaseAction(final String text, float duration, final int arrow, final boolean displayfinger){

		Action initAction = new Action() {

			@Override
			public boolean act(float delta) {
				textLabel = new Label(text, Resource.getDockerSkin());
				textLabel.setX(10);
				textLabel.setY(getStage().getHeight() - textLabel.getHeight() - 30);

				if(dimPixMap != null)
					dimPixMap.dispose();
				dimPixMap = new Pixmap((int)getStage().getWidth()+1, (int)getStage().getHeight(), Format.RGBA8888);
				dimPixMap.setColor(0f, 0f, 0f, 0.7f);
				dimPixMap.fillRectangle(
						0, 
						(int)(getStage().getHeight() - textLabel.getY() - textLabel.getHeight() - 20), 
						(int)getStage().getWidth() + 1, 
						(int)textLabel.getHeight() + 40
						);

				if(dimTexture != null)
					dimTexture.dispose();
				dimTexture = new Texture(dimPixMap);

				dimImage = new Image(dimTexture);
				dimImage.setPosition(0, 0);

				group.addActor(dimImage);
				group.addActor(textLabel);

				//Arrows
				if(arrow != NO_ARROW){
					Image arrowImage = null;
					if(arrow == ARROW_1){
						TextureRegion arrowRegion = Resource.getDockerSkinTextureAtlas().findRegion("small_curved_arrow");
						if(arrowRegion.isFlipY())
							arrowRegion.flip(false, true);							
						arrowImage = new Image(arrowRegion);
						arrowImage.setPosition(textLabel.getX() + textLabel.getWidth() - 30f, textLabel.getY() - arrowImage.getHeight() + 10f);
					}
					else if(arrow == ARROW_2){
						TextureRegion arrowRegion = Resource.getDockerSkinTextureAtlas().findRegion("small_curved_arrow");
						if(!arrowRegion.isFlipY())
						arrowRegion.flip(false, true);
						arrowImage = new Image(arrowRegion);
						arrowImage.setPosition(textLabel.getX() + textLabel.getWidth() +5f, textLabel.getY() - arrowImage.getHeight() + 30f);
					}
					else if(arrow == ARROW_3){
						TextureRegion arrowRegion = Resource.getDockerSkinTextureAtlas().findRegion("big_curved_arrow");
						arrowImage = new Image(arrowRegion);
						arrowImage.setPosition(getStage().getWidth()-arrowImage.getWidth()-10f, textLabel.getY() - 35f);
					}
					group.addActor(arrowImage);
				}

				//Finger
				if(displayfinger){
					TextureRegion fingerRegion = Resource.getDockerSkinTextureAtlas().findRegion("finger");
					final Image finger = new Image(fingerRegion);
					finger.setPosition(FINGER_INIT_X, FINGER_INIT_Y);

					setFingerAnimation(finger);

					group.addActor(finger);
				}

				getShip().clearPreviewContainer();
				
				//group starts transparent
				group.getColor().a = 0;
				return true;
			}
		};

		//Set up fade in/out and duration animation
		AlphaAction fadeIn = new AlphaAction();
		fadeIn.setAlpha(1);
		fadeIn.setInterpolation(Interpolation.fade);
		fadeIn.setDuration(1);

		DelayAction delay = new DelayAction();
		delay.setDuration(duration);

		AlphaAction fadeOut = new AlphaAction();
		fadeOut.setAlpha(0);
		fadeOut.setInterpolation(Interpolation.fade);
		fadeOut.setDuration(1);

		Action selfDestruct = new Action() {
			@Override
			public boolean act(float delta) {
				group.clearChildren();
				return true;
			}
		};

		SequenceAction baseSequence = new SequenceAction(initAction, fadeIn, delay, fadeOut, selfDestruct);

		return baseSequence;
	}

	private void setFingerAnimation(Actor finger){
		//Set up finger animation
		Action resetPosition = new Action() {

			@Override
			public boolean act(float delta) {
				actor.setColor(1f, 1f, 1f, 1f);
				actor.setPosition(FINGER_INIT_X, FINGER_INIT_Y);
				return true;
			}
		};

		MoveToAction slideRight = new MoveToAction();
		slideRight.setPosition(getStage().getWidth()/2, finger.getY());
		slideRight.setDuration(2);

		Action previewContainer = new Action() {

			@Override
			public boolean act(float delta) {
				int x = (int)((actor.getX() / getStage().getWidth()) * (float)Gdx.graphics.getWidth());
				int y = (int)actor.getY();
				previewPosition(x, y);
				return true;
			}
		};

		ParallelAction slide = new ParallelAction(slideRight, previewContainer);


		Action deploy = new Action() {			
			@Override
			public boolean act(float delta) {
				int x = (int)((actor.getX() / getStage().getWidth()) * (float)Gdx.graphics.getWidth());
				int y = (int)actor.getY();
				deployContainer(x, y);
				return true;
			}
		};

		MoveByAction touchUp = new MoveByAction();
		touchUp.setAmount(10f, 20f);
		touchUp.setDuration(0.5f);

		DelayAction delay = new DelayAction();
		delay.setDuration(0.25f);

		AlphaAction fadeOut = new AlphaAction();
		fadeOut.setAlpha(0f);
		fadeOut.setDuration(0.25f);

		SequenceAction moveSequence = new SequenceAction();
		moveSequence.addAction(slide);
		moveSequence.addAction(delay);
		moveSequence.addAction(deploy);
		moveSequence.addAction(touchUp);
		moveSequence.addAction(fadeOut);
		moveSequence.addAction(resetPosition);

		RepeatAction repeatAction = new RepeatAction();
		repeatAction.setAction(moveSequence);
		repeatAction.setCount(2);

		finger.addAction(repeatAction);
	}
	
	public Action getEndGameAction(){
		Action action = new Action() {
			
			@Override
			public boolean act(float delta) {
				endGame(0);
				return true;
			}
		};
		
		return action;
	}

	@Override
	public boolean canPlayerAct(int y) {
		return super.canPlayerAct(y) && group.getActions().size == 0;
	};

	@Override
	public Integer endGame(Integer gameScore) {
        application.showAds(true);
		application.returnToMenu();
		return null;
	}

	@Override
	public void startNewGame() {
		// not possible in Tutorial
	}

	@Override
	public void dispose() {
		this.dimPixMap.dispose();
		this.dimTexture.dispose();
		super.dispose();
	}
}

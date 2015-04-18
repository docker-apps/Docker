package com.docker.ui.menus;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonValue;
import com.docker.Docker;
import com.docker.domain.game.CareerGame;
import com.docker.domain.game.TutorialGame;
import com.docker.technicalservices.Persistence;
import com.docker.technicalservices.Resource;

/**
 * menu with a overview of all levels (locked and unlocked)
 */
public class CareerMenu extends AbstractMenu {
    private static final int LEVEL_BUTTON_PADDING = 5;
	private static final int LEVEL_BUTTON_WIDTH = 80;
	private Button backButton = createBackButton(skin);
    private final static float BACKBUTTON_PADDING_X = 5;
    private TextButton tutorialGameButton = new TextButton("How to", skin);
	private ScrollPane scrollpane;
	private TextureRegion arrowTexture;
    

    /**
     * @param application A reference to the Docker Application (Game) object.
     */
    public CareerMenu(final Docker application) {
        super(application);

		arrowTexture = Resource.getDockerSkinTextureAtlas().findRegion("straight_arrow");
        
        application.showAds(true);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.returnToLastScreen();
            }
        });
        backButton.setPosition(BACKBUTTON_PADDING_X, 13);
        backButton.setSize(40, 30);
        this.stage.addActor(backButton);
        Table careerTable = new Table();
        addLevelButtons(careerTable);
        careerTable.row();
        scrollpane = new AbstractScrollPane(careerTable);
        scrollpane.setPosition(0, 0);
        scrollpane.setSize(this.stage.getWidth(), this.stage.getHeight());
        scrollpane.setFlingTime(2);
        scrollpane.setupOverscroll(20, 30, 200);
        scrollpane.setFadeScrollBars(false);
        this.table.add(scrollpane).top();
    }

    /**
     * get all Levels and create buttons
     * set the buttons to inactive if the level isn't unlocked
     * @param table the table to add to buttons to
     */
    private void addLevelButtons(Table table) {
        List<JsonValue> allLevelPackages = Persistence.getAllLevelPackages();
        for (JsonValue levelPackage : allLevelPackages) {
        	Table packageTable = new Table(skin);
        	
        	//set package title
        	Label packageTitle = new Label(levelPackage.getString("title"), skin);
        	packageTable.add(packageTitle).center().colspan(3).row();
        	int i = 1;
        	
        	//special behaviour to include how-to in package nr1
			if(levelPackage.getInt("id") == 1){
				i = 2;
		        addTutorialButton(packageTable);
			}
			
			//add levels
			for (final JsonValue level : levelPackage.get("levels")) {
	            final String id = level.getString("id");
	            TextButton button = new TextButton(level.getString("name"), skin);
	            button.addListener(new ClickListener() {
	                @Override
	                public void clicked(InputEvent event, float x, float y) {
	                    application.showAds(false);
	                    application.setScreen(new CareerGame(application, id));
	                }
	            });
	            Boolean locked = Persistence.isLevelLocked(id);
	            if (locked) {
	                button.setTouchable(Touchable.disabled);
	                button.setText("Locked");
	            }
	            packageTable.add(button).fillX().width(LEVEL_BUTTON_WIDTH).pad(LEVEL_BUTTON_PADDING);
	            if (i % 3 == 0) {
	            	packageTable.row();
	            }
	            i++;
			}
			
			float fullPaddingSpace = this.stage.getWidth() - packageTable.getColumns() * (LEVEL_BUTTON_WIDTH+2*LEVEL_BUTTON_PADDING);
			float padLeft = fullPaddingSpace / 2;
			float backButtonSpace = backButton.getWidth()+BACKBUTTON_PADDING_X*2;
			if(padLeft < backButtonSpace)
				padLeft = backButtonSpace;
			float padRight = fullPaddingSpace - padLeft;
            table.add(packageTable).top().pad(0, padLeft, 0, padRight);
		}
    }

    private void addTutorialButton(Table table) {

        tutorialGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.showAds(false);
                application.setScreen(new TutorialGame(application));
            }
        });

        tutorialGameButton.setColor(0.5f, 1f, 0.5f, 1f);
        table.add(tutorialGameButton).fillX().width(LEVEL_BUTTON_WIDTH).pad(LEVEL_BUTTON_PADDING);
    }
    
    @Override
    public void render(float delta){
    	Batch batch = this.stage.getBatch();
    	batch.begin();
    	if(scrollpane.getScrollPercentX() != 0){
    		if(!arrowTexture.isFlipX())
    			arrowTexture.flip(true, false);
    		batch.draw(arrowTexture, 50, 5);
    	}

    	if(scrollpane.getScrollPercentX() != 1){
    		if(arrowTexture.isFlipX())
    			arrowTexture.flip(true, false);
    		batch.draw(arrowTexture, stage.getWidth()-50, 5);
    	}
    	batch.end();
    	
    	super.render(delta);

    	
    }

}

package com.docker.ui.menus;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.technicalservices.Persistence;
import com.docker.technicalservices.WorldStage;

public class InGameMenu extends AbstractMenu {
    Persistence persistence;
    
    private TextButton endGameButton = new TextButton("endgame", skin);
    private TextButton resumeButton = new TextButton("back", skin);
    private Label title = new Label("Pause",skin);
    
    private TextureRegion background;
    

    public InGameMenu(final Docker application, TextureRegion background) {
        super(application);
        this.background = background;
        this.stage = new WorldStage(viewport);
        
        this.persistence = application.persistence;
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setLastScreen();
            }
        });
        endGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.setMainmenu();
            }
        });

        table.add(title).left().padBottom(15).row().padBottom(10);
        table.add(endGameButton).size(100, 30).left().row();
        table.add(resumeButton).size(100, 30).center().row();
        stage.addActor(table);
        Image img = new Image(background);
        img.setPosition(0f, 0f);
        img.setColor(1f, 1f, 1f, 0.5f);

        ((WorldStage)stage).setBackground(img);
    }
}

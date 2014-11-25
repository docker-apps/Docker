package com.docker.ui.menus;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.technicalservices.Persistence;

public class InGameMenu extends AbstractMenu {
    Persistence persistence;

    private TextButton endGameButton = new TextButton("endgame", skin);
    private TextButton resumeButton = new TextButton("back", skin);
    private Label title = new Label("Pause",skin);
    

    public InGameMenu(final Docker application) {
        super(application);
        
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
    }
}

package com.docker.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.game.InfiniteGame;
import com.docker.domain.game.QuickGame;

/**
 *
 */
public class GameMenu implements Screen {
    Docker game;

    private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

    private Stage stage = new Stage();
    private Table table = new Table();

    private Label title = new Label("game menu",skin);
    private TextButton careerGameButton = new TextButton("Career game", skin);
    private TextButton quickGameButton = new TextButton("Quick game", skin);
    private TextButton infiniteGameButton = new TextButton("Infinite game", skin);
    private TextButton backButton = new TextButton("Back", skin);


    public GameMenu(final Docker game) {
        this.game = game;
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setLastScreen();
            }
        });
        careerGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CareerMenu(game));
            }
        });
        quickGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new QuickGame(game));
            }
        });
        infiniteGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new InfiniteGame(game));
            }
        });

        table.add(title).padBottom(40).row();
        table.add(careerGameButton).size(150, 60).padBottom(20).row();
        table.add(quickGameButton).size(150, 60).padBottom(20).row();
        table.add(infiniteGameButton).size(150, 60).padBottom(20).row();
        table.add(backButton).size(100, 40).left();

        table.setFillParent(true);
        stage.addActor(table);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE) ||
                Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            game.setLastScreen();
        }

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}

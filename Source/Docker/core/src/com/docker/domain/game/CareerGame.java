package com.docker.domain.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.docker.domain.gameobject.Background;
import com.docker.domain.gameobject.Container;
import com.docker.domain.gameobject.Crane;
import com.docker.domain.gameobject.Foreground;
import com.docker.domain.gameobject.Train;
import com.docker.technicalservices.WorldStage;

public class CareerGame extends AbstractGame {
    private static final double GAME_DURATION = 60;

    private WorldStage stage;
    private boolean isdeploying;
    private ExtendViewport viewport;
    private double timeLeft;

    public CareerGame(Game application, String levelId) {
		super(application);

        Level level = Level.loadLevel(levelId);
        this.setLives(level.getLifeCount());
        this.setShip(level.getShip());
        this.setTrain(level.getTrain());

        setTimeLeft(GAME_DURATION);
        setCrane(new Crane(80, WIDTH/2, HEIGHT));
        setLoadRating(new LoadRating(5, 5, 1));

        this.viewport = new ExtendViewport(WIDTH, HEIGHT);
        this.stage = new WorldStage(viewport){

            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                if(!getCrane().isDeploying() && getTrain().hasContainers()){
                    Container container = getTrain().getFirstContainer();
                    getShip().setPreviewContainer(getXPosition(x, y, container), container);
                }
                return true;
            }

            @Override
            public boolean touchDragged (int x, int y, int pointer) {
                if(!getCrane().isDeploying() && getTrain().hasContainers()){
                    Container container = getTrain().getFirstContainer();
                    getShip().setPreviewContainer(getXPosition(x, y, container),	container);
                }
                return true;
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                if(!getCrane().isDeploying() &&getTrain().hasContainers()){
                    Container container = getTrain().getFirstContainer();
                    Vector2 realCoords = getShip().getRealCoord(getXPosition(x, y, container), container);
                    if (realCoords != null) {
                        getCrane().deployContainer(
                                getTrain().removeContainer(),
                                getShip(),
                                realCoords.x,
                                realCoords.y);
                        return true;
                    }
                }
                return false;
            }

            private float getXPosition(int x, int y, Container container) {
                Vector2 touchCoords = new Vector2(x,y);
                touchCoords = getViewport().unproject(touchCoords);
                float containerPos = getContainerPos(touchCoords.x, container);
                return containerPos;
            }

        };
        Background background = new Background(this.stage.getWidth(), this.stage.getHeight());
        background.toBack();
        this.stage.setBackground(background);
        Foreground foreground = new Foreground(this.stage.getWidth());
        foreground.toFront();
        this.stage.setForeground(foreground);
        this.getShip().setZIndex(50);

        this.stage.addActor(getShip());
        Train train = getTrain();
        train.setX(0f);
        train.setY(HEIGHT-23);
        train.setSpeed(level.getTrainSpeed());
        this.stage.addActor(train);
        this.stage.addActor(getCrane());
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        this.stage.act(Gdx.graphics.getDeltaTime());

        if (getCrane().isDeploying() && !isdeploying) {
            isdeploying = true;
        }
        if (isdeploying && !getCrane().isDeploying()) {
            getLoadRating().calculate(getShip().getGrid());
            System.out.println(getLoadRating().getCapsizeValue());
            System.out.println(getLoadRating().getBreakValues().toString());
            isdeploying = false;
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.draw();
    }

    public float getContainerPos(float fingerPos, Container container){
        return fingerPos - (container.getLength() / 2) * container.getElementWidth();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.stage);
        Gdx.input.setCatchBackKey(true);
    }

    public void setTimeLeft(double timeLeft) {
        this.timeLeft = timeLeft;
    }
}

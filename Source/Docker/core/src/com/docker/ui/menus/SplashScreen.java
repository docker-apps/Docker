package com.docker.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.docker.Docker;
import com.docker.technicalservices.Resource;

public class SplashScreen extends AbstractMenu{

	public SplashScreen(Docker application) {
		super(application);

		TextureRegion logo_region = Resource.getDockerSkinTextureAtlas().findRegion("docker_title_logo");
		Image logo = new Image(logo_region);
		Label dockerLabel = new Label("Dockerapps", skin, "title");
		table.add(logo).center().row();
		table.add(dockerLabel).center();
	}
	
	public void proceed(){
		application.updateScreen(new MainMenu(application));
	}
	
    @Override
	public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        
        // handle input after rendering, as a consequence of the input-handling
        // can be the disposing of relevant resources.
    	handleInput();
	}	
}
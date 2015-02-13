package com.docker.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.docker.Docker;
import com.docker.IActivityRequestHandler;

public class HtmlLauncher extends GwtApplication implements IActivityRequestHandler{

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new Docker(this);
        }

    @Override
    public void showAds(boolean show) {

    }
}
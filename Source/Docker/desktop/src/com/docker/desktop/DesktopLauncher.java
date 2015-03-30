package com.docker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.docker.AdController;
import com.docker.Docker;

@SuppressWarnings("unused")
public class DesktopLauncher implements AdController {
    private static DesktopLauncher application;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height= 720;
		// Diese Zeile nur einkommentieren, wenn sich bilddateien gendaert haben.
		TexturePacker.process("../../../Documents/Grafik/deploy", "../android/assets/img", "docker");
//		TexturePacker.process("../../../Documents/Grafik/deploy_ui", "../android/assets/ui", "dockerskin");

        if (application == null) {
            application = new DesktopLauncher();
        }
		new LwjglApplication(new Docker(application), config);
	}

    @Override
    public void showAds(boolean show) {

	}

    @Override
    public void showInterstitialAd() {

    }
}

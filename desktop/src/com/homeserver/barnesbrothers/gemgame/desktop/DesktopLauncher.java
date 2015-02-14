package com.homeserver.barnesbrothers.gemgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.homeserver.barnesbrothers.gemgame.GemGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = GemGame.TITLE;
        config.width = GemGame.V_WIDTH * GemGame.SCALE;
        config.height = GemGame.V_HEIGHT * GemGame.SCALE;

		new LwjglApplication(new GemGame(), config);
	}
}

package com.homeserver.barnesbrothers.gemgame.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.homeserver.barnesbrothers.gemgame.GemGame;
import com.homeserver.barnesbrothers.gemgame.handlers.GameStateManager;

/**
 * Created by david on 2/9/15.
 */
public abstract class GameState {

    protected GameStateManager gsm;
    protected GemGame game;
    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        this.game = gsm.game();
        this.sb = game.getSpriteBatch();
        this.cam = game.getCamera();
        this.hudCam = game.getHudCam();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}

package com.homeserver.barnesbrothers.gemgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.homeserver.barnesbrothers.gemgame.handlers.Content;
import com.homeserver.barnesbrothers.gemgame.handlers.GameStateManager;
import com.homeserver.barnesbrothers.gemgame.handlers.GemInput;
import com.homeserver.barnesbrothers.gemgame.handlers.GemInputProcessor;


public class GemGame extends ApplicationAdapter {

    public static final String TITLE = "Gem Test";
    public static final int V_WIDTH = 1856;
    public static final int V_HEIGHT = 1080;
    public static final int SCALE = 2;

    public static final float STEP = 1/60f;
    //private float accum;

    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;

    private GameStateManager gsm;

    public static Content res;

    @Override
    public void create () {

        Gdx.input.setInputProcessor(new GemInputProcessor());

        res = new Content();
        res.loadTexture("gemtileset64.png", "tiles");
        sb = new SpriteBatch();
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        this.hudCam = new OrthographicCamera();
        this.hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        gsm = new GameStateManager(this);

    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //accum += Gdx.graphics.getDeltaTime();
        //while (accum <= STEP) {
        //    accum -= STEP;
            gsm.update(STEP);
            gsm.render();
        //}
        GemInput.update();
    }

    @Override
    public void dispose() {
        sb.dispose();
    }

    @Override
    public void resize(int w, int h) {

    }

    @Override
    public void pause() {
        Gdx.app.exit();
    }

    @Override
    public void resume() {}

    public SpriteBatch getSpriteBatch() { return sb; }
    public OrthographicCamera getCamera() { return cam; }
    public OrthographicCamera getHudCam() { return hudCam; }
}


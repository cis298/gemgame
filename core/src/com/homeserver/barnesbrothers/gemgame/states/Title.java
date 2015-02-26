package com.homeserver.barnesbrothers.gemgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.homeserver.barnesbrothers.gemgame.handlers.GameStateManager;

import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.PLAY;

/**
 * Created by david on 2/21/15.
 */
public class Title extends GameState {

    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tmr;

    private boolean startGame;
    private boolean screenIsTouched;

    public Title(GameStateManager gsm) {
        super(gsm);

        tileMap = new TmxMapLoader().load("maps/TitleScreen.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);

        if (Gdx.input.isTouched()) {
            screenIsTouched = false;
        } else {
            screenIsTouched = true;
        }

    }

    @Override
    public void handleInput() {
        if (Gdx.input.isTouched() && screenIsTouched) {
            startGame = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            startGame = true;
        }
        
        if (Gdx.input.justTouched()) {
            screenIsTouched = true;
        }

        if (startGame) {
            gsm.pushState(PLAY);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render() {
        tmr.setView(cam);
        tmr.render();
    }

    @Override
    public void dispose() {

    }
}

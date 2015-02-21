package com.homeserver.barnesbrothers.gemgame.states;

import com.badlogic.gdx.Gdx;
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

    public Title(GameStateManager gsm) {
        super(gsm);

        tileMap = new TmxMapLoader().load("maps/TitleScreen.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);

        if (Gdx.input.isTouched()) {
            startGame = false;
        } else {
            startGame = true;
        }

    }

    @Override
    public void handleInput() {
        if (Gdx.input.isTouched() && startGame) {
            gsm.pushState(PLAY);
        }
        if (Gdx.input.justTouched()) {
            startGame = true;
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

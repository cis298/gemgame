package com.homeserver.barnesbrothers.gemgame.handlers;

import com.homeserver.barnesbrothers.gemgame.GemGame;
import com.homeserver.barnesbrothers.gemgame.states.GameState;
import com.homeserver.barnesbrothers.gemgame.states.Play;

import java.util.Stack;

/**
 * Created by david on 2/9/15.
 */
public class GameStateManager {

    private GemGame game;

    private Stack<GameState> gameStates;

    public static final int PLAY = 912837;

    public GemGame game() { return game; }

    public GameStateManager(GemGame game) {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(PLAY);
    }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    private GameState getState(int state) {
        if(state == PLAY) {
            return new Play(this);
        }
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }
}

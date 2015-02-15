package com.homeserver.barnesbrothers.gemgame.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by david on 2/14/15.
 */
public class GemInputProcessor extends InputAdapter {

    public boolean keyDown(int k) {
        if (k == Keys.W) {
            GemInput.setKey(GemInput.UP, true);
        }
        if (k == Keys.S) {
            GemInput.setKey(GemInput.DOWN, true);
        }
        return true;
    }

    public boolean keyUp(int k) {
        if (k == Keys.W) {
            GemInput.setKey(GemInput.UP, false);
        }
        if (k == Keys.S) {
            GemInput.setKey(GemInput.DOWN, false);
        }
        return true;
    }
}

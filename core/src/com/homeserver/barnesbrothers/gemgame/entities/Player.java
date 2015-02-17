package com.homeserver.barnesbrothers.gemgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.homeserver.barnesbrothers.gemgame.GemGame;
import com.homeserver.barnesbrothers.gemgame.handlers.B2DVars;

import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.SSIZE;

/**
 * Created by david on 2/13/15.
 */
public class Player extends B2DSprite {

    private short currentAttunement;

    private TextureRegion[] sprites;
    private Texture tex;

    public Player(Body body) {
        super(body);

        currentAttunement = B2DVars.REDATTUNEMENT;

        tex = GemGame.res.getTexture("tiles");
        TextureRegion sprite = TextureRegion.split(tex, SSIZE, SSIZE)[0][0];
        sprites = new TextureRegion[1];
        sprites[0] = sprite;

        setAnimation(sprites, 1 / 12f);
    }

    public short getCurrentAttunement() {
        return currentAttunement;
    }

    public void setCurrentAttunement(short attunement) {
        currentAttunement = attunement;
    }

    public void updateTexture() {
        sprites[0] = TextureRegion.split(tex, SSIZE, SSIZE)[0][currentAttunement];
        setAnimation(sprites, 1 / 12f);
    }
}

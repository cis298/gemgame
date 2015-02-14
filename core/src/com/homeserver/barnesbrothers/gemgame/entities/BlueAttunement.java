package com.homeserver.barnesbrothers.gemgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.homeserver.barnesbrothers.gemgame.GemGame;

/**
 * Created by david on 2/13/15.
 */
public class BlueAttunement extends B2DSprite {
    public BlueAttunement(Body body) {
        super(body);

        Texture tex = GemGame.res.getTexture("tiles");
        TextureRegion sprite = TextureRegion.split(tex, 32, 32)[2][3];
        TextureRegion[] sprites = new TextureRegion[1];
        sprites[0] = sprite;

        setAnimation(sprites, 1 / 12f);
    }
}

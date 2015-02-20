package com.homeserver.barnesbrothers.gemgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.homeserver.barnesbrothers.gemgame.GemGame;
import com.homeserver.barnesbrothers.gemgame.handlers.B2DVars;

import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.PPM;
import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.SSIZE;

/**
 * Created by david on 2/13/15.
 */
public class Player extends B2DSprite {

    private short currentAttunement;

    private TextureRegion[] sprites;
    private Texture tex;

    private boolean stuckAtZeroV;

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

    @Override
    public void update(float dt) {
        animation.update(dt);
        //Check to see if the player has no x velocity
        if (this.getBody().getLinearVelocity().x != 0.0f) {
            stuckAtZeroV = false;
        }

        //Check to see if the player is at one side of the map or the other, and then change direction.
        //if (player.getPosition().x >= GemGame.V_WIDTH / PPM -player.getWidth()/2/PPM) {
        if (this.getPosition().x >= (GemGame.V_WIDTH - (this.getWidth()/2) - (SSIZE*3))/PPM) {
            this.getBody().setLinearVelocity(-4.0f, 0);
        }
        //if (player.getPosition().x <= 0 + player.getWidth()/2/PPM) {
        if (this.getPosition().x <= ((SSIZE*3) + (this.getWidth()/2))/PPM) {
            this.getBody().setLinearVelocity(4.0f, 0);
        }

        //Check to see if the velocity drops bellow 4 in one direction or another, and if so reset it.
        if (this.getBody().getLinearVelocity().x < 4.0f && this.getBody().getLinearVelocity().x > 0.0f) {
            this.getBody().setLinearVelocity(4.0f, this.getBody().getLinearVelocity().y);
        }
        if (this.getBody().getLinearVelocity().x > -4.0f && this.getBody().getLinearVelocity().x < 0.0f) {
            this.getBody().setLinearVelocity(-4.0f, this.getBody().getLinearVelocity().y);
        }

        //This checks to see if the x velocity has hit zero and then tries to resolve it by making
        //the velocity 4, and setting a bool to check in the next loop. The direction may be the wrong direction
        if (this.getBody().getLinearVelocity().x == 0.0f && !stuckAtZeroV) {
            this.getBody().setLinearVelocity(4.0f, this.getBody().getLinearVelocity().y);
            stuckAtZeroV = true;
        } else if(this.getBody().getLinearVelocity().x == 0.0f && stuckAtZeroV) {
            this.getBody().setLinearVelocity(-4.0f, this.getBody().getLinearVelocity().y);
            stuckAtZeroV = false;
        }
    }

}

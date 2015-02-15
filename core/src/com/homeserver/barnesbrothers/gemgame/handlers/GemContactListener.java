package com.homeserver.barnesbrothers.gemgame.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.homeserver.barnesbrothers.gemgame.GemGame;
import com.homeserver.barnesbrothers.gemgame.entities.B2DSprite;
import com.homeserver.barnesbrothers.gemgame.entities.Player;

/**
 * Created by david on 2/14/15.
 */
public class GemContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;

        if(fa.getUserData() != null && fa.getUserData().equals("player")) {
            //System.out.println(fa.getUserData());
            //System.out.println(fb.getUserData());
            //System.out.println();
            if (fb.getUserData().equals("Gem")) {
                //change direction
                fa.getBody().setLinearVelocity(-1 * fa.getBody().getLinearVelocity().x, fa.getBody().getLinearVelocity().y);
            } else if (fb.getUserData().equals("Attunement")) {
                //Change attunement
                setAttunement(fb.getBody().getUserData(), fa.getBody().getUserData());
            } else {

            }
        }
        if(fb.getUserData() != null && fb.getUserData().equals("player")) {
            //System.out.println(fa.getUserData());
            //System.out.println(fb.getUserData());
            //System.out.println();
            if (fa.getUserData().equals("Gem")) {
                //change direction
                fb.getBody().setLinearVelocity(-1 * fb.getBody().getLinearVelocity().x, fb.getBody().getLinearVelocity().y);
            } else if (fa.getUserData().equals("Attunement")) {
                //Change attunement
                setAttunement(fa.getBody().getUserData(), fb.getBody().getUserData());
            } else {

            }

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void setAttunement(Object className, Object player) {
        Player localPlayer = (Player)player;
        System.out.println(className.toString().split("@")[0].split("\\.")[5]);
        if(className.toString().split("@")[0].split("\\.")[5].equals("RedAttunement")) {
            localPlayer.setCurrentAttunement(B2DVars.REDATTUNEMENT);
            localPlayer.updateTexture();
        }
        if(className.toString().split("@")[0].split("\\.")[5].equals("YellowAttunement")) {
            localPlayer.setCurrentAttunement(B2DVars.YELLOWATTUNEMENT);
            localPlayer.updateTexture();
        }
        if(className.toString().split("@")[0].split("\\.")[5].equals("GreenAttunement")) {
            localPlayer.setCurrentAttunement(B2DVars.GREENATTUNEMENT);
            localPlayer.updateTexture();
        }
        if(className.toString().split("@")[0].split("\\.")[5].equals("BlueAttunement")) {
            localPlayer.setCurrentAttunement(B2DVars.BLUEATTUNEMENT);
            localPlayer.updateTexture();
        }
    }
}

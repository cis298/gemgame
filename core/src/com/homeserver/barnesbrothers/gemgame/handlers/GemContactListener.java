package com.homeserver.barnesbrothers.gemgame.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.homeserver.barnesbrothers.gemgame.entities.Player;
import java.util.HashMap;

/**
 * Created by david on 2/14/15.
 */
public class GemContactListener implements ContactListener {

    private Array<Body> redGemsToRemove;
    private Array<Body> yellowGemsToRemove;
    private Array<Body> greenGemsToRemove;
    private Array<Body> blueGemsToRemove;

    private boolean removeExit;

    private HashMap<String, Integer> gemToAttunement;

    public GemContactListener() {
        super();
        redGemsToRemove = new Array<Body>();
        yellowGemsToRemove = new Array<Body>();
        greenGemsToRemove = new Array<Body>();
        blueGemsToRemove = new Array<Body>();

        gemToAttunement = new HashMap<String, Integer>();
        gemToAttunement.put("RedGem", 0);
        gemToAttunement.put("YellowGem", 1);
        gemToAttunement.put("GreenGem", 2);
        gemToAttunement.put("BlueGem", 3);

        removeExit = false;
    }


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
                queueGemsToRemoveGem(fb, fa);
            } else if (fb.getUserData().equals("Attunement")) {
                //Change attunement
                setAttunement(fb.getBody().getUserData(), fa.getBody().getUserData());
            } else if (fb.getUserData().equals("Exit")) {
                removeExit = true;
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
                queueGemsToRemoveGem(fa, fb);
            } else if (fa.getUserData().equals("Attunement")) {
                //Change attunement
                setAttunement(fa.getBody().getUserData(), fb.getBody().getUserData());
            } else if (fa.getUserData().equals("Exit")) {
                removeExit = true;
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

    public Array<Body> getRedGemsToRemove() {
        return redGemsToRemove;
    }
    public Array<Body> getYellowGemsToRemove() {
        return yellowGemsToRemove;
    }
    public Array<Body> getGreenGemsToRemove() {
        return greenGemsToRemove;
    }
    public Array<Body> getBlueGemsToRemove() {
        return blueGemsToRemove;
    }

    public boolean getRemoveExit() {
        return removeExit;
    }

    private void queueGemsToRemoveGem(Fixture className, Fixture player) {
        Player localPlayer = (Player)player.getBody().getUserData();
        Body gemBody = (Body)className.getBody();

        String gemName = className.getBody().getUserData().toString().split("@")[0].split("\\.")[6];

        if(localPlayer.getCurrentAttunement() == 0 && gemToAttunement.get(gemName) == 0) {
            redGemsToRemove.add(gemBody);
        } else if(localPlayer.getCurrentAttunement() == 1 && gemToAttunement.get(gemName) == 1) {
            yellowGemsToRemove.add(gemBody);
        } else if(localPlayer.getCurrentAttunement() == 2 && gemToAttunement.get(gemName) == 2) {
            greenGemsToRemove.add(gemBody);
        } else if(localPlayer.getCurrentAttunement() == 3 && gemToAttunement.get(gemName) == 3) {
            blueGemsToRemove.add(gemBody);
        }
    }

    private void setAttunement(Object className, Object player) {
        Player localPlayer = (Player)player;

        if(className.toString().split("@")[0].split("\\.")[6].equals("RedAttunement")) {
            localPlayer.setCurrentAttunement(B2DVars.REDATTUNEMENT);
            localPlayer.updateTexture();
        }
        if(className.toString().split("@")[0].split("\\.")[6].equals("YellowAttunement")) {
            localPlayer.setCurrentAttunement(B2DVars.YELLOWATTUNEMENT);
            localPlayer.updateTexture();
        }
        if(className.toString().split("@")[0].split("\\.")[6].equals("GreenAttunement")) {
            localPlayer.setCurrentAttunement(B2DVars.GREENATTUNEMENT);
            localPlayer.updateTexture();
        }
        if(className.toString().split("@")[0].split("\\.")[6].equals("BlueAttunement")) {
            localPlayer.setCurrentAttunement(B2DVars.BLUEATTUNEMENT);
            localPlayer.updateTexture();
        }
    }
}

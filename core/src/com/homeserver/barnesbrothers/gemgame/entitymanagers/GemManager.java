package com.homeserver.barnesbrothers.gemgame.entitymanagers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.homeserver.barnesbrothers.gemgame.entities.B2DSprite;
import com.homeserver.barnesbrothers.gemgame.entities.gems.BlueGem;
import com.homeserver.barnesbrothers.gemgame.entities.gems.GreenGem;
import com.homeserver.barnesbrothers.gemgame.entities.gems.RedGem;
import com.homeserver.barnesbrothers.gemgame.entities.gems.YellowGem;
import com.homeserver.barnesbrothers.gemgame.handlers.B2DVars;
import com.homeserver.barnesbrothers.gemgame.handlers.GemContactListener;

import java.util.HashMap;

import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.HSSIZE;
import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.PPM;

/**
 * Created by david on 2/19/15.
 */
public class GemManager {

    private HashMap<String, Array<B2DSprite>> gems;

    public GemManager() {
        gems = new HashMap<String, Array<B2DSprite>>();
        gems.put("RedGems",new Array<B2DSprite>());
        gems.put("YellowGems",new Array<B2DSprite>());
        gems.put("GreenGems",new Array<B2DSprite>());
        gems.put("BlueGems",new Array<B2DSprite>());
    }

    public HashMap<String, Array<B2DSprite>> getGems() {
        return gems;
    }

    public void setGems(HashMap<String, Array<B2DSprite>> gems) {
        this.gems = gems;
    }

    public void createGems(String[] layerNames,TiledMap tiledMap, World world) {

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape pshape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;

        fdef.shape = pshape;
        fdef.isSensor = false;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;

        pshape.setAsBox((HSSIZE - 8) / PPM, (HSSIZE - 8) / PPM);

        for (String layerName : layerNames) {

            MapLayer layer = tiledMap.getLayers().get(layerName);

            for (MapObject mo : layer.getObjects()) {

                float x = ((Float) mo.getProperties().get("x") + HSSIZE) / PPM;
                float y = ((Float) mo.getProperties().get("y") + HSSIZE) / PPM;

                bdef.position.set(x, y);

                Body body = world.createBody(bdef);

                if (layerName.equals("RedGems")) {
                    RedGem redGem = new RedGem(body);
                    gems.get(layerName).add(redGem);
                    body.setUserData(redGem);
                    fdef.filter.categoryBits = B2DVars.BIT_RED_GEM;
                } else if (layerName.equals("YellowGems")) {
                    YellowGem yellowGem = new YellowGem(body);
                    gems.get(layerName).add(yellowGem);
                    body.setUserData(yellowGem);
                    fdef.filter.categoryBits = B2DVars.BIT_YELLOW_GEM;
                } else if (layerName.equals("GreenGems")) {
                    GreenGem greenGem = new GreenGem(body);
                    gems.get(layerName).add(greenGem);
                    body.setUserData(greenGem);
                    fdef.filter.categoryBits = B2DVars.BIT_GREEN_GEM;
                } else if (layerName.equals("BlueGems")) {
                    BlueGem blueGem = new BlueGem(body);
                    gems.get(layerName).add(blueGem);
                    body.setUserData(blueGem);
                    fdef.filter.categoryBits = B2DVars.BIT_BLUE_GEM;
                }
                body.createFixture(fdef).setUserData("Gem");
            }
        }
    }

    public void removeGems(World world, GemContactListener cl) {
        //Remove Gems
        Array<Body> redGemsToRemove = cl.getRedGemsToRemove();
        Array<Body> yellowGemsToRemove = cl.getYellowGemsToRemove();
        Array<Body> greenGemsToRemove = cl.getGreenGemsToRemove();
        Array<Body> blueGemsToRemove = cl.getBlueGemsToRemove();

        for(int i = 0; i < redGemsToRemove.size; i++) {
            Body b = redGemsToRemove.get(i);
            //redGems.removeValue((RedGem) b.getUserData(), true);
            gems.get("RedGems").removeValue((RedGem) b.getUserData(), true);
            world.destroyBody(b);
        }
        for(int i = 0; i < yellowGemsToRemove.size; i++) {
            Body b = yellowGemsToRemove.get(i);
            //yellowGems.removeValue((YellowGem) b.getUserData(), true);
            gems.get("YellowGems").removeValue((YellowGem) b.getUserData(), true);
            world.destroyBody(b);
        }
        for(int i = 0; i < greenGemsToRemove.size; i++) {
            Body b = greenGemsToRemove.get(i);
            //greenGems.removeValue((GreenGem) b.getUserData(), true);
            gems.get("GreenGems").removeValue((GreenGem) b.getUserData(), true);
            world.destroyBody(b);
        }
        for(int i = 0; i < blueGemsToRemove.size; i++) {
            Body b = blueGemsToRemove.get(i);
            //blueGems.removeValue((BlueGem) b.getUserData(), true);
            gems.get("BlueGems").removeValue((BlueGem) b.getUserData(), true);
            world.destroyBody(b);
        }

        redGemsToRemove.clear();
        yellowGemsToRemove.clear();
        greenGemsToRemove.clear();
        blueGemsToRemove.clear();
    }
}

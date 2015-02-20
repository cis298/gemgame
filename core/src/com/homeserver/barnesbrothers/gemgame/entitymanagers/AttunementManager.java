package com.homeserver.barnesbrothers.gemgame.entitymanagers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.homeserver.barnesbrothers.gemgame.entities.B2DSprite;
import com.homeserver.barnesbrothers.gemgame.entities.attunements.BlueAttunement;
import com.homeserver.barnesbrothers.gemgame.entities.attunements.GreenAttunement;
import com.homeserver.barnesbrothers.gemgame.entities.attunements.RedAttunement;
import com.homeserver.barnesbrothers.gemgame.entities.attunements.YellowAttunement;
import com.homeserver.barnesbrothers.gemgame.entities.gems.BlueGem;
import com.homeserver.barnesbrothers.gemgame.entities.gems.GreenGem;
import com.homeserver.barnesbrothers.gemgame.entities.gems.RedGem;
import com.homeserver.barnesbrothers.gemgame.entities.gems.YellowGem;
import com.homeserver.barnesbrothers.gemgame.handlers.B2DVars;

import java.util.HashMap;

import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.HSSIZE;
import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.PPM;

/**
 * Created by david on 2/19/15.
 */
public class AttunementManager {

    private HashMap<String, Array<B2DSprite>> attunements;

    public AttunementManager() {
        attunements = new HashMap<String, Array<B2DSprite>>();
        attunements.put("RedAttunement", new Array<B2DSprite>());
        attunements.put("YellowAttunement", new Array<B2DSprite>());
        attunements.put("GreenAttunement", new Array<B2DSprite>());
        attunements.put("BlueAttunement", new Array<B2DSprite>());
    }

    public HashMap<String, Array<B2DSprite>> getAttunements() {
        return attunements;
    }

    public void setAttunements(HashMap<String, Array<B2DSprite>> gems) {
        this.attunements = gems;
    }

    public void createAttunements(String[] layerNames,TiledMap tiledMap, World world) {

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape pshape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;

        fdef.shape = pshape;
        fdef.isSensor = true;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;

        pshape.setAsBox((HSSIZE - 8) / PPM, (HSSIZE - 8) / PPM);

        for (String layerName : layerNames) {

            MapLayer layer = tiledMap.getLayers().get(layerName);

            for (MapObject mo : layer.getObjects()) {

                float x = ((Float) mo.getProperties().get("x") + HSSIZE) / PPM;
                float y = ((Float) mo.getProperties().get("y") + HSSIZE) / PPM;

                bdef.position.set(x, y);

                Body body = world.createBody(bdef);

                if (layerName.equals("RedAttunement")) {
                    RedAttunement redAttunement = new RedAttunement(body);
                    attunements.get(layerName).add(redAttunement);
                    body.setUserData(redAttunement);
                    fdef.filter.categoryBits = B2DVars.BIT_RED_ATTUNEMENT;
                } else if (layerName.equals("YellowAttunement")) {
                    YellowAttunement yellowAttunement = new YellowAttunement(body);
                    attunements.get(layerName).add(yellowAttunement);
                    body.setUserData(yellowAttunement);
                    fdef.filter.categoryBits = B2DVars.BIT_YELLOW_ATTUNEMENT;
                } else if (layerName.equals("GreenAttunement")) {
                    GreenAttunement greenAttunement = new GreenAttunement(body);
                    attunements.get(layerName).add(greenAttunement);
                    body.setUserData(greenAttunement);
                    fdef.filter.categoryBits = B2DVars.BIT_GREEN_ATTUNEMENT;
                } else if (layerName.equals("BlueAttunement")) {
                    BlueAttunement blueAttunement = new BlueAttunement(body);
                    attunements.get(layerName).add(blueAttunement);
                    body.setUserData(blueAttunement);
                    fdef.filter.categoryBits = B2DVars.BIT_BLUE_ATTUNEMENT;
                }
                body.createFixture(fdef).setUserData("Attunement");
            }
        }
    }
}

package com.homeserver.barnesbrothers.gemgame.entitymanagers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.homeserver.barnesbrothers.gemgame.entities.B2DSprite;
import com.homeserver.barnesbrothers.gemgame.entities.Spike;
import com.homeserver.barnesbrothers.gemgame.handlers.B2DVars;

import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.HSSIZE;
import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.PPM;

/**
 * Created by david on 2/20/15.
 */
public class SpikeManager {

    private Array<B2DSprite> spikes;

    public SpikeManager() {
        spikes = new Array<B2DSprite>();
    }

    public Array<B2DSprite> getSpikes() {
        return spikes;
    }

    public void setSpikes(Array<B2DSprite> spikes) {
        this.spikes = spikes;
    }

    public void createSpikes(String layerName,TiledMap tiledMap, World world) {

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape pshape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;

        fdef.shape = pshape;
        fdef.isSensor = false;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;

        pshape.setAsBox((HSSIZE - 8) / PPM, (HSSIZE - 8) / PPM);


        MapLayer layer = tiledMap.getLayers().get(layerName);

        for (MapObject mo : layer.getObjects()) {

            float x = ((Float) mo.getProperties().get("x") + HSSIZE) / PPM;
            float y = ((Float) mo.getProperties().get("y") + HSSIZE) / PPM;

            bdef.position.set(x, y);

            Body body = world.createBody(bdef);

            Spike spike = new Spike(body);
            spikes.add(spike);
            body.setUserData(spike);
            fdef.filter.categoryBits = B2DVars.BIT_SPIKE;

            body.createFixture(fdef).setUserData("Spike");
        }
    }
}

package com.homeserver.barnesbrothers.gemgame.entitymanagers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.homeserver.barnesbrothers.gemgame.entities.B2DSprite;
import com.homeserver.barnesbrothers.gemgame.entities.Exit;
import com.homeserver.barnesbrothers.gemgame.entities.Player;
import com.homeserver.barnesbrothers.gemgame.handlers.B2DVars;

import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.HSSIZE;
import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.PPM;
import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.SSIZE;

/**
 * Created by david on 2/20/15.
 */
public class PlayerAndExitManager {

    private B2DSprite exit;
    private Player player;

    public B2DSprite getExit() {
        return exit;
    }

    public void setExit(B2DSprite exit) {
        this.exit = exit;
    }

    public PlayerAndExitManager() {

    }

    public Player createPlayer(TiledMap tiledMap, World world) {

        short playerInteraction = B2DVars.BIT_RED_GEM | B2DVars.BIT_YELLOW_GEM | B2DVars.BIT_GREEN_GEM | B2DVars.BIT_BLUE_GEM |
                B2DVars.BIT_RED_ATTUNEMENT | B2DVars.BIT_YELLOW_ATTUNEMENT | B2DVars.BIT_GREEN_ATTUNEMENT | B2DVars.BIT_BLUE_ATTUNEMENT |
                B2DVars.BIT_SPIKE | B2DVars.BIT_EXIT;

        CircleShape shape = new CircleShape();
        shape.setRadius((HSSIZE-8) / PPM);

        BodyDef bdef = new BodyDef();

        bdef.position.set(((3*SSIZE) + HSSIZE) / PPM, (14*SSIZE + HSSIZE)/PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = playerInteraction;
        body.createFixture(fdef).setUserData("player");

        player = new Player(body);
        body.setUserData(player);

        return player;
    }

    public void createExit(String layerName,TiledMap tiledMap, World world) {
        MapLayer layer = tiledMap.getLayers().get(layerName);

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        MapObject mo = layer.getObjects().get(0);
        //for(MapObject mo : layer.getObjects()) {
            float x = ((Float) mo.getProperties().get("x") + HSSIZE)/PPM;
            float y = ((Float) mo.getProperties().get("y") + HSSIZE)/PPM;

            bdef.position.set(x, y);

            PolygonShape pshape = new PolygonShape();
            pshape.setAsBox(HSSIZE/PPM, HSSIZE/PPM);

            fdef.shape = pshape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = B2DVars.BIT_EXIT;
            fdef.filter.maskBits = B2DVars.BIT_PLAYER;

            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("Exit");

            Exit exit = new Exit(body);
            body.setUserData(exit);
            //exits.add(exit);
        //}
        this.exit = exit;
    }
}

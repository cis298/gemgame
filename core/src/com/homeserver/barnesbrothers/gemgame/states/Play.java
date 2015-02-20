package com.homeserver.barnesbrothers.gemgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.homeserver.barnesbrothers.gemgame.entities.*;
import com.homeserver.barnesbrothers.gemgame.GemGame;
import com.homeserver.barnesbrothers.gemgame.entities.attunements.BlueAttunement;
import com.homeserver.barnesbrothers.gemgame.entities.attunements.GreenAttunement;
import com.homeserver.barnesbrothers.gemgame.entities.attunements.RedAttunement;
import com.homeserver.barnesbrothers.gemgame.entities.attunements.YellowAttunement;
import com.homeserver.barnesbrothers.gemgame.entities.gems.BlueGem;
import com.homeserver.barnesbrothers.gemgame.entities.gems.GreenGem;
import com.homeserver.barnesbrothers.gemgame.entities.gems.RedGem;
import com.homeserver.barnesbrothers.gemgame.entities.gems.YellowGem;
import com.homeserver.barnesbrothers.gemgame.entitymanagers.AttunementManager;
import com.homeserver.barnesbrothers.gemgame.entitymanagers.GemManager;
import com.homeserver.barnesbrothers.gemgame.entitymanagers.PlayerAndExitManager;
import com.homeserver.barnesbrothers.gemgame.entitymanagers.SpikeManager;
import com.homeserver.barnesbrothers.gemgame.handlers.B2DVars;
import com.homeserver.barnesbrothers.gemgame.handlers.GameStateManager;
import com.homeserver.barnesbrothers.gemgame.handlers.GemContactListener;
import com.homeserver.barnesbrothers.gemgame.handlers.GemInput;

import java.util.Map;

import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.*;
import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.SSIZE;

/**
 * Created by david on 2/9/15.
 */
public class Play extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;
    private GemContactListener cl;

    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tmr;

    private Player player;
    private Exit exit;

    private GemManager gemManager;
    private AttunementManager attunementManager;
    private SpikeManager spikeManager;
    private PlayerAndExitManager playerAndExitManager;

    //private Array<Spike> spikes;

    public Play(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2(0,0), true);
        cl = new GemContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        // load tile map
        tileMap = new TmxMapLoader().load("maps/GemGameTestLevel.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);

        gemManager = new GemManager();
        attunementManager = new AttunementManager();
        spikeManager = new SpikeManager();
        playerAndExitManager = new PlayerAndExitManager();

        //spikes = new Array<Spike>();


        short playerInteraction = B2DVars.BIT_RED_GEM | B2DVars.BIT_YELLOW_GEM | B2DVars.BIT_GREEN_GEM | B2DVars.BIT_BLUE_GEM |
                                B2DVars.BIT_RED_ATTUNEMENT | B2DVars.BIT_YELLOW_ATTUNEMENT | B2DVars.BIT_GREEN_ATTUNEMENT | B2DVars.BIT_BLUE_ATTUNEMENT |
                                B2DVars.BIT_SPIKE | B2DVars.BIT_EXIT;

        createPlayer(B2DVars.BIT_PLAYER, playerInteraction, BodyDef.BodyType.DynamicBody);
        //createEntity("Exit", B2DVars.BIT_EXIT, B2DVars.BIT_PLAYER, BodyDef.BodyType.StaticBody);

        //createEntities("Spikes", B2DVars.BIT_SPIKE, B2DVars.BIT_PLAYER, BodyDef.BodyType.StaticBody);
        //playerAndExitManager.createPlayer(tileMap, world);

        playerAndExitManager.createExit("Exit", tileMap, world);
        //this.exit = (Exit)playerAndExitManager.getExit();

        spikeManager.createSpikes("Spikes", tileMap, world);

        String[] gemLayers = {"RedGems","YellowGems","GreenGems","BlueGems"};
        gemManager.createGems(gemLayers, tileMap, world);
        String[] attunementLayers = {"RedAttunement","YellowAttunement","GreenAttunement","BlueAttunement"};
        attunementManager.createAttunements(attunementLayers, tileMap, world);

        player.getBody().setLinearVelocity(4.0f, 0);

        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, GemGame.V_WIDTH / PPM, GemGame.V_HEIGHT / PPM);
    }

    @Override
    public void handleInput() {
        if (GemInput.isDown(GemInput.UP) && player.getPosition().y <= (SSIZE*15 - (player.getHeight()/2))/PPM) {
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 1.5f);
        } else if (GemInput.isDown(GemInput.DOWN) && player.getPosition().y >= (0 + (player.getHeight()/2))/PPM) {
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, -1.5f);
        } else {
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0.0f);
        }

        if (Gdx.input.isTouched()) {

            Vector3 touch = b2dCam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            float playerY = player.getBody().getPosition().y;

            //Adding an epsilon here so that the player doesn't vibrate. Might be a better way to do this.
            if (touch.y > playerY+.01 && playerY <= (SSIZE*15 - (player.getHeight() / 2)) / PPM) {
                player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 1.5f);
            } else if (touch.y < playerY-.01 && playerY >= (0 + (player.getHeight() / 2)) / PPM) {
                player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, -1.5f);
            } else {
                player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0.0f);
            }
        }
    }

    @Override
    public void update(float dt) {

        handleInput();

        world.step(dt, 6, 2);

        for (Map.Entry<String, Array<B2DSprite>> entry : gemManager.getGems().entrySet()) {
            for (B2DSprite gem : entry.getValue()) {
                gem.update(dt);
            }
        }

        //for (Map.Entry<String, Array<B2DSprite>> entry : attunementManager.getAttunements().entrySet()) {
        //    for (B2DSprite attunement : entry.getValue()) {
        //        attunement.update(dt);
        //    }
        //}

        //Update the spikes
        //for(int i = 0; i < spikes.size; i++) {
        //    spikes.get(i).update(dt);
        //}

        //Update the player and the exit
        player.update(dt);
        //exit.update(dt);

        gemManager.removeGems(world, cl);

        //Level done. Remove exit and player, then start new.
        if (cl.getRemoveExit()) {
            world.destroyBody(playerAndExitManager.getExit().getBody());
            world.destroyBody(player.getBody());
            gsm.pushState(PLAY);
        }
    }

    @Override
    public void render() {
        tmr.setView(cam);
        tmr.render();

        sb.setProjectionMatrix(cam.combined);

        for (Map.Entry<String, Array<B2DSprite>> entry : gemManager.getGems().entrySet()) {
            for (B2DSprite gem : entry.getValue()) {
                gem.render(sb);
            }
        }

        for (Map.Entry<String, Array<B2DSprite>> entry : attunementManager.getAttunements().entrySet()) {
            for (B2DSprite attunement : entry.getValue()) {
                attunement.render(sb);
            }
        }

        //Draw the spikes
        for (B2DSprite spike : spikeManager.getSpikes()) {
            spike.render(sb);
        }
        //for(int i = 0; i < spikes.size; i++) {
        //    spikes.get(i).render(sb);
        //}

        //Draw the player and exit
        //exit.render(sb);
        playerAndExitManager.getExit().render(sb);
        player.render(sb);

        //Render the world
        //b2dr.render(world, b2dCam.combined);
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
    }

    private void createPlayer(short categoryBit, short maskBit, BodyDef.BodyType bType) {
        CircleShape shape = new CircleShape();
        shape.setRadius((HSSIZE-8) / PPM);

        BodyDef bdef = new BodyDef();
        //bdef.position.set(HSSIZE / PPM, (GemGame.V_HEIGHT - HSSIZE)/PPM);
        bdef.position.set(((3*SSIZE) + HSSIZE) / PPM, (14*SSIZE + HSSIZE)/PPM);
        bdef.type = bType;
        Body body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = categoryBit;
        fdef.filter.maskBits = maskBit;
        body.createFixture(fdef).setUserData("player");

        player = new Player(body);
        body.setUserData(player);
    }

    private void createEntity(String layerName, short categoryBit, short maskBit, BodyDef.BodyType bType) {
        MapLayer layer = tileMap.getLayers().get(layerName);

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        bdef.type = bType;

        for(MapObject mo : layer.getObjects()) {
            float x = ((Float) mo.getProperties().get("x") + HSSIZE)/PPM;
            float y = ((Float) mo.getProperties().get("y") + HSSIZE)/PPM;

            bdef.position.set(x, y);

            PolygonShape pshape = new PolygonShape();
            pshape.setAsBox(HSSIZE/PPM, HSSIZE/PPM);

            fdef.shape = pshape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = categoryBit;
            fdef.filter.maskBits = maskBit;

            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("Exit");

            Exit exit = new Exit(body);
            this.exit = exit;
            body.setUserData(exit);
        }
    }

    private void createEntities(String layerName, short categoryBit, short maskBit, BodyDef.BodyType bType) {

        MapLayer layer = tileMap.getLayers().get(layerName);

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for(MapObject mo : layer.getObjects()) {
            bdef.type = bType;

            float x = ((Float) mo.getProperties().get("x") + HSSIZE)/PPM;
            float y = ((Float) mo.getProperties().get("y") + HSSIZE)/PPM;

            bdef.position.set(x,y);

            PolygonShape pshape = new PolygonShape();
            pshape.setAsBox((HSSIZE-8)/PPM,(HSSIZE-8)/PPM);

            fdef.shape = pshape;
            fdef.isSensor = false;
            fdef.filter.categoryBits = categoryBit;
            fdef.filter.maskBits = maskBit;

            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("Spike");
            Spike spike = new Spike(body);
            //spikes.add(spike);
            body.setUserData(spike);

        }

    }
}

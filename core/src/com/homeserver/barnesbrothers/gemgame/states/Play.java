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

    private GemManager gemManager;
    private AttunementManager attunementManager;
    private SpikeManager spikeManager;
    private PlayerAndExitManager playerAndExitManager;

    public Play(GameStateManager gsm) {
        super(gsm);

        //Create world, contact listener, abd debugger.
        world = new World(new Vector2(0,0), true);
        cl = new GemContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        // load tile map
        tileMap = new TmxMapLoader().load("maps/GemGameTestLevel.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);

        //Create the managers for the entities
        gemManager = new GemManager();
        attunementManager = new AttunementManager();
        spikeManager = new SpikeManager();
        playerAndExitManager = new PlayerAndExitManager();

        //Create the Player, Exit, and Spikes
        playerAndExitManager.createPlayer(tileMap, world);
        playerAndExitManager.createExit("Exit", tileMap, world);
        spikeManager.createSpikes("Spikes", tileMap, world);

        //Create the Gems and Attunements
        String[] gemLayers = {"RedGems","YellowGems","GreenGems","BlueGems"};
        gemManager.createGems(gemLayers, tileMap, world);
        String[] attunementLayers = {"RedAttunement","YellowAttunement","GreenAttunement","BlueAttunement"};
        attunementManager.createAttunements(attunementLayers, tileMap, world);

        //Set the initial speed for the player
        playerAndExitManager.getPlayer().getBody().setLinearVelocity(B2DVars.SPEED, 0);

        //Setup Cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, GemGame.V_WIDTH / PPM, GemGame.V_HEIGHT / PPM);
    }

    @Override
    public void handleInput() {
        //Make a local variable for the player so we don't have to constantly get it from the manager.
        Player player = (Player)playerAndExitManager.getPlayer();

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

        //Update the Gems ### May not need this ###
        for (Map.Entry<String, Array<B2DSprite>> entry : gemManager.getGems().entrySet()) {
            for (B2DSprite gem : entry.getValue()) {
                gem.update(dt);
            }
        }

        //Update the Attunements ### Not Needed ###
        //for (Map.Entry<String, Array<B2DSprite>> entry : attunementManager.getAttunements().entrySet()) {
        //    for (B2DSprite attunement : entry.getValue()) {
        //        attunement.update(dt);
        //    }
        //}


        //Update the player
        playerAndExitManager.getPlayer().update(dt);

        gemManager.removeGems(world, cl);

        //Level done. Remove exit and player, then start new.
        if (cl.getRemoveExit()) {
            world.destroyBody(playerAndExitManager.getExit().getBody());
            world.destroyBody(playerAndExitManager.getPlayer().getBody());
            gsm.pushState(PLAY);
        }
    }

    @Override
    public void render() {
        tmr.setView(cam);
        tmr.render();

        sb.setProjectionMatrix(cam.combined);

        //Draw the Gems
        for (Map.Entry<String, Array<B2DSprite>> entry : gemManager.getGems().entrySet()) {
            for (B2DSprite gem : entry.getValue()) {
                gem.render(sb);
            }
        }

        //Draw the attunements
        for (Map.Entry<String, Array<B2DSprite>> entry : attunementManager.getAttunements().entrySet()) {
            for (B2DSprite attunement : entry.getValue()) {
                attunement.render(sb);
            }
        }

        //Draw the spikes
        for (B2DSprite spike : spikeManager.getSpikes()) {
            spike.render(sb);
        }

        //Draw the player and exit
        playerAndExitManager.getExit().render(sb);
        playerAndExitManager.getPlayer().render(sb);

        //Render the world
        //b2dr.render(world, b2dCam.combined);
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
    }
}

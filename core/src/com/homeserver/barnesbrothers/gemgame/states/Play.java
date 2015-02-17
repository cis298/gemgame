package com.homeserver.barnesbrothers.gemgame.states;

import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.PPM;

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
import com.homeserver.barnesbrothers.gemgame.handlers.B2DVars;
import com.homeserver.barnesbrothers.gemgame.handlers.GameStateManager;
import com.homeserver.barnesbrothers.gemgame.handlers.GemContactListener;
import com.homeserver.barnesbrothers.gemgame.handlers.GemInput;

import static com.homeserver.barnesbrothers.gemgame.handlers.B2DVars.PLAY;

/**
 * Created by david on 2/9/15.
 */
public class Play extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;
    private GemContactListener cl;

    private TiledMap tileMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;

    private Player player;
    private Exit exit;

    private Array<RedGem> redGems;
    private Array<YellowGem> yellowGems;
    private Array<GreenGem> greenGems;
    private Array<BlueGem> blueGems;

    private Array<RedAttunement> redAttunments;
    private Array<YellowAttunement> yellowAttunments;
    private Array<GreenAttunement> greenAttunments;
    private Array<BlueAttunement> blueAttunements;

    private Array<Spike> spikes;

    private boolean stuckAtZeroV;

    public Play(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2(0,0), true);
        cl = new GemContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        // load tile map
        tileMap = new TmxMapLoader().load("maps/GemTest2.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);

        redGems = new Array<RedGem>();
        yellowGems = new Array<YellowGem>();
        greenGems = new Array<GreenGem>();
        blueGems = new Array<BlueGem>();

        redAttunments = new Array<RedAttunement>();
        yellowAttunments = new Array<YellowAttunement>();
        greenAttunments = new Array<GreenAttunement>();
        blueAttunements = new Array<BlueAttunement>();

        spikes = new Array<Spike>();

        stuckAtZeroV = false;

        short playerInteraction = B2DVars.BIT_RED_GEM | B2DVars.BIT_YELLOW_GEM | B2DVars.BIT_GREEN_GEM | B2DVars.BIT_BLUE_GEM |
                                B2DVars.BIT_RED_ATTUNMENT | B2DVars.BIT_YELLOW_ATTUNMENT | B2DVars.BIT_GREEN_ATTUNMENT | B2DVars.BIT_BLUE_ATTUNMENT |
                                B2DVars.BIT_SPIKE | B2DVars.BIT_EXIT;

        createPlayer(B2DVars.BIT_PLAYER, playerInteraction, BodyDef.BodyType.DynamicBody);
        createEntity("Exit", B2DVars.BIT_EXIT, B2DVars.BIT_PLAYER, BodyDef.BodyType.StaticBody);

        createEntities("RedGems", B2DVars.BIT_RED_GEM, B2DVars.BIT_PLAYER, BodyDef.BodyType.StaticBody);
        createEntities("YellowGems", B2DVars.BIT_YELLOW_GEM, B2DVars.BIT_PLAYER, BodyDef.BodyType.StaticBody);
        createEntities("GreenGems", B2DVars.BIT_GREEN_GEM, B2DVars.BIT_PLAYER, BodyDef.BodyType.StaticBody);
        createEntities("BlueGems", B2DVars.BIT_BLUE_GEM, B2DVars.BIT_PLAYER, BodyDef.BodyType.StaticBody);

        createEntities("RedAttunment", B2DVars.BIT_RED_ATTUNMENT, B2DVars.BIT_PLAYER, BodyDef.BodyType.StaticBody);
        createEntities("YellowAttunment", B2DVars.BIT_YELLOW_ATTUNMENT, B2DVars.BIT_PLAYER, BodyDef.BodyType.StaticBody);
        createEntities("GreenAttunment", B2DVars.BIT_GREEN_ATTUNMENT, B2DVars.BIT_PLAYER, BodyDef.BodyType.StaticBody);
        createEntities("BlueAttunment", B2DVars.BIT_BLUE_ATTUNMENT, B2DVars.BIT_PLAYER, BodyDef.BodyType.StaticBody);

        createEntities("Spikes", B2DVars.BIT_SPIKE, B2DVars.BIT_PLAYER, BodyDef.BodyType.StaticBody);

        player.getBody().setLinearVelocity(4.0f, 0);

        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, GemGame.V_WIDTH / PPM, GemGame.V_HEIGHT / PPM);
    }

    @Override
    public void handleInput() {
        if (GemInput.isDown(GemInput.UP) && player.getPosition().y <= (GemGame.V_HEIGHT - (player.getHeight()/2))/PPM) {
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
            if (touch.y > playerY+.01 && playerY <= (GemGame.V_HEIGHT - (player.getHeight() / 2)) / PPM) {
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

        //Update all of the Gems
        for(int i = 0; i < redGems.size; i++) {
            redGems.get(i).update(dt);
        }
        for(int i = 0; i < yellowGems.size; i++) {
            yellowGems.get(i).update(dt);
        }
        for(int i = 0; i < greenGems.size; i++) {
            greenGems.get(i).update(dt);
        }
        for(int i = 0; i < blueGems.size; i++) {
            blueGems.get(i).update(dt);
        }

        //Update all of the attunements
        for(int i = 0; i < redAttunments.size; i++) {
            redAttunments.get(i).update(dt);
        }
        for(int i = 0; i < yellowAttunments.size; i++) {
            yellowAttunments.get(i).update(dt);
        }
        for(int i = 0; i < greenAttunments.size; i++) {
            greenAttunments.get(i).update(dt);
        }
        for(int i = 0; i < blueAttunements.size; i++) {
            blueAttunements.get(i).update(dt);
        }

        //Update the spikes
        for(int i = 0; i < spikes.size; i++) {
            spikes.get(i).update(dt);
        }

        //Update the player and the exit
        player.update(dt);
        exit.update(dt);

        //Check to see if the player has no x velocity
        if (player.getBody().getLinearVelocity().x != 0.0f) {
            stuckAtZeroV = false;
        }

        //Check to see if the player is at one side of the map or the other, and then change direction.
        if (player.getPosition().x >= GemGame.V_WIDTH / PPM -player.getWidth()/2/PPM) {
            player.getBody().setLinearVelocity(-4.0f, 0);
        }
        if (player.getPosition().x <= 0 + player.getWidth()/2/PPM) {
            player.getBody().setLinearVelocity(4.0f, 0);
        }

        //Check to see if the velocity drops bellow 4 in one direction or another, and if so reset it.
        if (player.getBody().getLinearVelocity().x < 4.0f && player.getBody().getLinearVelocity().x > 0.0f) {
            player.getBody().setLinearVelocity(4.0f, player.getBody().getLinearVelocity().y);
        }
        if (player.getBody().getLinearVelocity().x > -4.0f && player.getBody().getLinearVelocity().x < 0.0f) {
            player.getBody().setLinearVelocity(-4.0f, player.getBody().getLinearVelocity().y);
        }

        //This checks to see if the x velocity has hit zero and then tries to resolve it by making
        //the velocity 4, and setting a bool to check in the next loop. The direction may be the wrong direction
        if (player.getBody().getLinearVelocity().x == 0.0f && !stuckAtZeroV) {
            player.getBody().setLinearVelocity(4.0f, player.getBody().getLinearVelocity().y);
            stuckAtZeroV = true;
        } else if(player.getBody().getLinearVelocity().x == 0.0f && stuckAtZeroV) {
            player.getBody().setLinearVelocity(-4.0f, player.getBody().getLinearVelocity().y);
            stuckAtZeroV = false;
        }

        //Remove Gems
        Array<Body> redGemsToRemove = cl.getRedGemsToRemove();
        Array<Body> yellowGemsToRemove = cl.getYellowGemsToRemove();
        Array<Body> greenGemsToRemove = cl.getGreenGemsToRemove();
        Array<Body> blueGemsToRemove = cl.getBlueGemsToRemove();

        for(int i = 0; i < redGemsToRemove.size; i++) {
            Body b = redGemsToRemove.get(i);
            redGems.removeValue((RedGem) b.getUserData(), true);
            world.destroyBody(b);
        }
        for(int i = 0; i < yellowGemsToRemove.size; i++) {
            Body b = yellowGemsToRemove.get(i);
            yellowGems.removeValue((YellowGem) b.getUserData(), true);
            world.destroyBody(b);
        }
        for(int i = 0; i < greenGemsToRemove.size; i++) {
            Body b = greenGemsToRemove.get(i);
            greenGems.removeValue((GreenGem) b.getUserData(), true);
            world.destroyBody(b);
        }
        for(int i = 0; i < blueGemsToRemove.size; i++) {
            Body b = blueGemsToRemove.get(i);
            blueGems.removeValue((BlueGem) b.getUserData(), true);
            world.destroyBody(b);
        }

        redGemsToRemove.clear();
        yellowGemsToRemove.clear();
        greenGemsToRemove.clear();
        blueGemsToRemove.clear();

        //Level done. Remove exit and player, then start new.
        if (cl.getRemoveExit()) {
            world.destroyBody(exit.getBody());
            world.destroyBody(player.getBody());
            gsm.pushState(PLAY);
        }
    }

    @Override
    public void render() {
        tmr.setView(cam);
        tmr.render();

        sb.setProjectionMatrix(cam.combined);

        //Draw the Gems
        for(int i = 0; i < redGems.size; i++) {
            redGems.get(i).render(sb);
        }
        for(int i = 0; i < yellowGems.size; i++) {
            yellowGems.get(i).render(sb);
        }
        for(int i = 0; i < greenGems.size; i++) {
            greenGems.get(i).render(sb);
        }
        for(int i = 0; i < blueGems.size; i++) {
            blueGems.get(i).render(sb);
        }

        //Draw the attunements
        for(int i = 0; i < redAttunments.size; i++) {
            redAttunments.get(i).render(sb);
        }
        for(int i = 0; i < yellowAttunments.size; i++) {
            yellowAttunments.get(i).render(sb);
        }
        for(int i = 0; i < greenAttunments.size; i++) {
            greenAttunments.get(i).render(sb);
        }
        for(int i = 0; i < blueAttunements.size; i++) {
            blueAttunements.get(i).render(sb);
        }

        //Draw the spikes
        for(int i = 0; i < spikes.size; i++) {
            spikes.get(i).render(sb);
        }

        //Draw the player and exit
        exit.render(sb);
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
        shape.setRadius(12 / PPM);

        BodyDef bdef = new BodyDef();
        bdef.position.set(16 / PPM, (GemGame.V_HEIGHT - 16)/PPM);
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
            float x = ((Float) mo.getProperties().get("x") + 16)/PPM;
            float y = ((Float) mo.getProperties().get("y") + 16)/PPM;

            bdef.position.set(x, y);

            PolygonShape pshape = new PolygonShape();
            pshape.setAsBox(16.0f/PPM, 16.0f/PPM);

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

            float x = ((Float) mo.getProperties().get("x") + 16)/PPM;
            float y = ((Float) mo.getProperties().get("y") + 16)/PPM;

            bdef.position.set(x,y);

            PolygonShape pshape = new PolygonShape();
            pshape.setAsBox(12.0f/PPM,12.0f/PPM);

            fdef.shape = pshape;
            fdef.isSensor = false;
            fdef.filter.categoryBits = categoryBit;
            fdef.filter.maskBits = maskBit;

            Body body = world.createBody(bdef);

            if (layerName.equals("RedGems")) {
                body.createFixture(fdef).setUserData("Gem");
                RedGem redGem = new RedGem(body);
                redGems.add(redGem);
                body.setUserData(redGem);
            } else if (layerName.equals("YellowGems")) {
                body.createFixture(fdef).setUserData("Gem");
                YellowGem yellowGem = new YellowGem(body);
                yellowGems.add(yellowGem);
                body.setUserData(yellowGem);
            } else if (layerName.equals("GreenGems")) {
                body.createFixture(fdef).setUserData("Gem");
                GreenGem greenGem = new GreenGem(body);
                greenGems.add(greenGem);
                body.setUserData(greenGem);
            } else if (layerName.equals("BlueGems")) {
                body.createFixture(fdef).setUserData("Gem");
                BlueGem blueGem = new BlueGem(body);
                blueGems.add(blueGem);
                body.setUserData(blueGem);
            } else if (layerName.equals("RedAttunment")) {
                fdef.isSensor = true;
                body.createFixture(fdef).setUserData("Attunement");
                RedAttunement redAttunement = new RedAttunement(body);
                redAttunments.add(redAttunement);
                body.setUserData(redAttunement);
            } else if (layerName.equals("YellowAttunment")) {
                fdef.isSensor = true;
                body.createFixture(fdef).setUserData("Attunement");
                YellowAttunement yellowAttunement = new YellowAttunement(body);
                yellowAttunments.add(yellowAttunement);
                body.setUserData(yellowAttunement);
            } else if (layerName.equals("GreenAttunment")) {
                fdef.isSensor = true;
                body.createFixture(fdef).setUserData("Attunement");
                GreenAttunement greenAttunement = new GreenAttunement(body);
                greenAttunments.add(greenAttunement);
                body.setUserData(greenAttunement);
            } else if (layerName.equals("BlueAttunment")) {
                fdef.isSensor = true;
                body.createFixture(fdef).setUserData("Attunement");
                BlueAttunement blueAttunement = new BlueAttunement(body);
                blueAttunements.add(blueAttunement);
                body.setUserData(blueAttunement);
            } else {
                body.createFixture(fdef).setUserData("Spike");
                Spike spike = new Spike(body);
                spikes.add(spike);
                body.setUserData(spike);
            }

        }

    }
}

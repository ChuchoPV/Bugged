package com.jpv.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jpv.Level1;
import com.jpv.Scenes.Hud;
import com.jpv.Sprites.Character;
import com.jpv.Sprites.Enemies.Enemy;
import com.jpv.Sprites.Enemies.TheRedBug;
import com.jpv.Sprites.Items.Heart;
import com.jpv.Sprites.Items.Item;
import com.jpv.Sprites.Items.ItemDef;
import com.jpv.Sprites.TileObjects.Obstacules;
import com.jpv.Tools.B2WorldCreator;
import com.jpv.Tools.WorldContactListener;

import java.security.Key;
import java.util.concurrent.LinkedBlockingDeque;

public class PlayScreen implements Screen {
    //Instance of out game
    private Level1 game;

    private TextureAtlas atlas;

    //Basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    //Hearts and score
    private Hud hud;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;  //Represent all the fetures and bodies indside our 2Dbox World
    private B2WorldCreator creator;

    //Creating out Character and other sprites
    private Character player;

    private Array<Item> items;
    private LinkedBlockingDeque<ItemDef> itemsToSpawn;

    private int timerBoss;
    private long startTime;
    private boolean first;

    public PlayScreen(Level1 game){
        atlas = new TextureAtlas("ATLAS_FINAL.pack");
        //Our game
        this.game = game;
        gamecam = new OrthographicCamera();
        //creat a FitViewport to maintain virtual aspect radio despite screen size
        gamePort = new FitViewport(Level1.V_WIDTH / Level1.PPM, Level1.V_HEIGHT / Level1.PPM, gamecam);
         //The map atributes
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("City_Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Level1.PPM);
        //The camara set at the world
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() /2,0);
        //Setting the variables of our world
        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();
        //b2dr.setDrawBodies(false);
        b2dr.SHAPE_STATIC.set(0,0,0,1);
        //Instance of out player
        player = new Character(this);
        creator = new B2WorldCreator(this);
        world.setContactListener(new WorldContactListener());
        hud = new Hud(this);
        timerBoss = 1;
        startTime = TimeUtils.nanoTime();
        first = true;

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingDeque<ItemDef>();

    }

    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    private void handleSpawingItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            items.add(new Heart(this, idef.position.x, idef.position.y));
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    private void update(float dt){
        //handle the input to move around our world
        handleInput(dt);
        handleSpawingItems();

        world.step(1/60f,6,2);
        player.update(dt);

        if(player.boss) {
            manageBoss(dt);
        }

        for(Enemy enemy : creator.getMosquitos()) {
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 224 / Level1.PPM)
                enemy.b2body.setActive(true);
        }
        creator.getTheRedBug().update(dt);

        for(Item item : items)
            item.update(dt);
        if(gamecam.position.x>190) {
            gamecam.position.x = 190.1f;
            if(gamecam.position.y > 3.6f) {
                gamecam.position.y -= 0.1f;
            }
            for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
                if(object.getProperties().containsKey("Boss"))
                    new Obstacules(this,object);
            }
            player.boss=true;
        }
        else {
            if (player.b2body.getPosition().x >= (Level1.V_WIDTH / 2 / Level1.PPM))
                gamecam.position.x = player.b2body.getPosition().x;

            if (player.b2body.getPosition().y >= (Level1.V_HEIGHT / 2 / Level1.PPM)
                    && player.b2body.getPosition().y <= ((Level1.V_HEIGHT + 360) / Level1.PPM))
                gamecam.position.y = player.b2body.getPosition().y;
        }

        gamecam.update();
        //Setting the camara to our map
        renderer.setView(gamecam);
    }

    private void manageBoss(float dt) {
        if (TimeUtils.timeSinceNanos(startTime) > 2000000000 && timerBoss != 0) {
            // if time passed since the time you set startTime at is more than 1 second

            //your code here
            //Gdx.app.log("Tiempo",""+timerBoss);
            //Gdx.app.log("StartTimer",""+startTime);
            if(first) {
                creator.getTheRedBug().b2body.applyLinearImpulse(new Vector2(-5f, 9f), creator.getTheRedBug().b2body.getWorldCenter(), true);
                first = false;
                timerBoss +=1;
            }else {
                creator.getTheRedBug().b2body.applyLinearImpulse(new Vector2(5f, 9f), creator.getTheRedBug().b2body.getWorldCenter(), true);
                first = true;
                timerBoss +=1;
            }

            //also you can set the new startTime
            //so this block will execute every one second
            startTime = TimeUtils.nanoTime();
        }if(timerBoss == 6){
            Gdx.app.log("Tiempo",""+timerBoss);
            startTime = 0;
            timerBoss = 0;
        }
    }

    private void handleInput(float dt) {
        if(player.currentState != Character.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.DPAD_UP)){// &&
                    //player.b2body.getPosition().y < (Level1.V_HEIGHT + 500) / Level1.PPM
                    //&& (player.currentState==Character.State.RUNNING || player.currentState==Character.State.STANDING)) {
                player.b2body.applyLinearImpulse(new Vector2(0, 8f), player.b2body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) && player.b2body.getLinearVelocity().x <= 2 && player.currentState != Character.State.DAMAGED) {
                player.b2body.applyLinearImpulse(new Vector2(0.5f, 0), player.b2body.getWorldCenter(), true);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) && player.b2body.getLinearVelocity().x >= -2 && player.currentState != Character.State.DAMAGED){
                player.b2body.applyLinearImpulse(new Vector2(-0.5f, 0), player.b2body.getWorldCenter(), true);
            }
            if(player.currentState==Character.State.RUNNING && !(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)))
                if(player.b2body.getLinearVelocity().x>0 || player.b2body.getLinearVelocity().x<0)
                    player.b2body.setLinearVelocity(0,0);
            //aqui es donde se tiene que poner el inicio de la animacion con algo asi como un timer.
        }

    }

    @Override
    public void render(float delta) {
        update(delta);

        //Clear the game screen with black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //renderer our game map
        renderer.render();
        //renderer our Box2DDebbugerLines
        b2dr.render(world,gamecam.combined);

        //Rendering everything
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        //hud.heart.draw(game.batch);
        player.draw(game.batch);
        for(Enemy enemy : creator.getMosquitos())
            enemy.draw(game.batch);
        creator.getTheRedBug().draw(game.batch);
        for(Item item : items)
            item.draw(game.batch);
        game.batch.end();
        Hud.stage.draw();

        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

    }

    private boolean gameOver(){
        if(player.currentState == Character.State.DEAD &&  player.getStateTimer() > 3){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);

    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public Character getPlayer() {
        return player;
    }

    public B2WorldCreator getCreator() { return creator; }

    public Level1 getGame() {
        return game;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }
}

package com.jpv.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jpv.Level1;
import com.jpv.Scenes.Hud;
import com.jpv.Sprites.Character;
import com.jpv.Sprites.Enemies.Enemy;
import com.jpv.Sprites.Items.Heart;
import com.jpv.Sprites.Items.Item;
import com.jpv.Sprites.Items.ItemDef;
import com.jpv.Tools.B2WorldCreator;
import com.jpv.Tools.WorldContactListener;
import com.sun.tools.javac.jvm.Items;

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

    //Tiled map variables
    private TmxMapLoader mapLoader;
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
    private Sprite heart;


    public PlayScreen(Level1 game){
        atlas = new TextureAtlas("Bugged_1.pack");
        //Out game
        this.game = game;
        gamecam = new OrthographicCamera();
        //creat a FitViewport to maintain virtual aspect radio despite screen size
        gamePort = new FitViewport(Level1.V_WIDTH / Level1.PPM, Level1.V_HEIGHT / Level1.PPM, gamecam);
         //The map atributes
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("City_Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Level1.PPM);
        //The camara set at the world
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() /2,0);
        //Setting the variables of our world
        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();
        b2dr.SHAPE_STATIC.set(0,0,0,1);
        //Instance of out player
        player = new Character(this);

        //The HUD Instnace
        hud = new Hud(game.batch, Level1.V_WIDTH / Level1.PPM, Level1.V_HEIGHT /Level1.PPM,
                player.b2body.getPosition().x / Level1.PPM, player.b2body.getPosition().y / Level1.PPM);

        creator = new B2WorldCreator(this);

        world.setContactListener(new WorldContactListener());

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingDeque<ItemDef>();

    }

    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    public void handleSpawingItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Heart.class){
                items.add(new Heart(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public float getDT(float dt){
        return dt;
    }

    public void update(float dt){
        //handle the input to move around our world
        handleInput(dt);
        handleSpawingItems();

        world.step(1/60f,6,2);
        getDT(dt);
        player.update(dt);
        for(Enemy enemy : creator.getMosquitos()) {
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 224 / Level1.PPM)
                enemy.b2body.setActive(true);
        }

        for(Item item : items)
            item.update(dt);

        if(player.b2body.getPosition().x >= (Level1.V_WIDTH / 2 / Level1.PPM))
            gamecam.position.x = player.b2body.getPosition().x;

        if(player.b2body.getPosition().y >= (Level1.V_HEIGHT / 2 / Level1.PPM)
                && player.b2body.getPosition().y <= ((Level1.V_HEIGHT + 360) / Level1.PPM))
            gamecam.position.y = player.b2body.getPosition().y;

        gamecam.update();
        //Setting the camara to our map
        renderer.setView(gamecam);
    }

    private void handleInput(float dt) {
        if(player.currentState != Character.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.DPAD_UP) &&
                    player.b2body.getPosition().y < (Level1.V_HEIGHT + 500) / Level1.PPM)
                player.b2body.applyLinearImpulse(new Vector2(0, 8f), player.b2body.getWorldCenter(), true);

            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
                player.b2body.applyLinearImpulse(new Vector2(0.5f, 0), player.b2body.getWorldCenter(), true);
                //player.b2body.getPosition().set(new Vector2(05.f, 0));
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) && player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(-0.5f, 0), player.b2body.getWorldCenter(), true);
                //player.b2body.setLinearVelocity(-1.4f,-0f);
            }
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
        player.draw(game.batch);
        for(Enemy enemy : creator.getMosquitos())
            enemy.draw(game.batch);
        for(Item item : items)
            item.draw(game.batch);
        game.batch.end();

        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

    }

    public boolean gameOver(){
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

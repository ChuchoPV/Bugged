package com.jpv.Bugged.Niveles.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jpv.Bugged.Niveles.LevelManager;
import com.jpv.Bugged.Niveles.Scenes.Hud;
import com.jpv.Bugged.Niveles.Sprites.Character;
import com.jpv.Bugged.Niveles.Sprites.Enemies.Enemy;
import com.jpv.Bugged.Niveles.Sprites.Items.Heart;
import com.jpv.Bugged.Niveles.Sprites.Items.Item;
import com.jpv.Bugged.Niveles.Sprites.Items.ItemDef;
import com.jpv.Bugged.Niveles.Sprites.Items.Proyectil;
import com.jpv.Bugged.Niveles.Sprites.Items.ProyectilHank;
import com.jpv.Bugged.Niveles.Sprites.TileObjects.Obstacules;
import com.jpv.Bugged.Niveles.Tools.B2WorldCreator;
import com.jpv.Bugged.Niveles.Tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingDeque;

public class PlayScreen implements Screen {
    //region VARIABLES
    //Instance of out game
    private LevelManager game;
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

    private Array<Item> hearts;
    private Array<Item> proyectiles;
    private Array<ProyectilHank> proyectilesHank;
    private LinkedBlockingDeque<ItemDef> heartsToSpawn;
    private LinkedBlockingDeque<ItemDef> proyectilToSpawn;
    private LinkedBlockingDeque<ItemDef> proyectilesHankToSpawn;

    private float timerBoss;
    private long startTime;
    private float stopBossTimer;
    private boolean first;

    public boolean updateObjets;
    private String enemyType;
    private boolean isHank;

    private int level;

    //endregion

    public PlayScreen(LevelManager game, int level){
        atlas = new TextureAtlas("ATLAS_Final.pack");
        //Our game
        this.game = game;
        //Set the number of level
        this.level = level;
        gamecam = new OrthographicCamera();
        //creat a FitViewport to maintain virtual aspect radio despite screen size
        gamePort = new FitViewport(LevelManager.V_WIDTH / LevelManager.PPM, LevelManager.V_HEIGHT / LevelManager.PPM, gamecam);
         //The map atributes
        TmxMapLoader mapLoader = new TmxMapLoader();
        if(level == 1){
            map = mapLoader.load("City_Map.tmx");
        }else if(level == 2){
            map = mapLoader.load("Subs_Map.tmx");
        }else if(level == 3){
            map = mapLoader.load("Mountain_Map.tmx");
        }else if(level == 4){
            map = mapLoader.load("Cave_Map.tmx");
        }else{
            map = mapLoader.load("Final.tmx");
        }
        renderer = new OrthogonalTiledMapRenderer(map, 1 / LevelManager.PPM);
        //The camara set at the world
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() /2,0);
        //Setting the variables of our world
        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();
        //b2dr.setDrawBodies(false);
        b2dr.SHAPE_STATIC.set(0,0,0,1);
        this.isHank = game.isHank();
        //Instance of out player
        player = new Character(this);
        creator = new B2WorldCreator(this);
        world.setContactListener(new WorldContactListener());
        hud = new Hud(this);
        timerBoss = 0;
        startTime = 0;
        stopBossTimer = 0;
        first = true;

        hearts = new Array<Item>();
        proyectiles = new Array<Item>();
        proyectilesHank = new Array<ProyectilHank>();
        heartsToSpawn = new LinkedBlockingDeque<ItemDef>();
        proyectilToSpawn = new LinkedBlockingDeque<ItemDef>();
        proyectilesHankToSpawn = new LinkedBlockingDeque<ItemDef>();

        updateObjets = true;
        enemyType = "";
    }

    //region SPAWN ITEMS
    private boolean fliped;
    public void spawnItem(ItemDef idef, boolean fliped) {
        this.fliped=fliped;
        if(proyectilesHankToSpawn.isEmpty()) {
            if (idef.type == Proyectil.class) {
                proyectilToSpawn.add(idef);
            }
            if (idef.type == ProyectilHank.class) {
                proyectilesHankToSpawn.add(idef);
            }
        }else if(idef.type == Heart.class){
            heartsToSpawn.add(idef);
        }
    }

    private void handleSpawingItems(){
        if(!proyectilesHankToSpawn.isEmpty()){
            ItemDef idef = proyectilesHankToSpawn.pop();
            proyectilesHank.add(new ProyectilHank(this, idef.position.x, idef.position.y,fliped));
        }
        if(!heartsToSpawn.isEmpty()){
            ItemDef idef = heartsToSpawn.pop();
            hearts.add(new Heart(this, idef.position.x, idef.position.y));
        }if(!proyectilToSpawn.isEmpty()){
            ItemDef idef = proyectilToSpawn.pop();
            proyectiles.add(new Proyectil(this, idef.position.x, idef.position.y, enemyType,fliped));
        }
    }

    //endregion

    private void update(float dt){
        //handle the input to move around our world
        handleInput(dt);
        handleSpawingItems();

        world.step(1/60f,6,2);
        player.update(dt);
        //Code to optimize
        /*for(Platforms platforms : creator.getPlatforms()){
            platforms.update();
        }
        for(Obstacules obstacules : creator.getObstacules()){
            obstacules.update();
        }*/

        if(player.boss) {
            for(Enemy enemy : creator.getMosquitos()) {
                enemy.destroy();
            }
            manageBoss(dt);
        }

        for(Enemy enemy : creator.getMosquitos()) {
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 224 / LevelManager.PPM) {
                enemy.b2body.setActive(true);
            }
        }
        for(Enemy enemy : creator.getSlugs()) {
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 224 / LevelManager.PPM) {
                enemy.b2body.setActive(true);
                enemy.setShot(true);
            }
        }for(Enemy enemy : creator.getSpiders()) {
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 224 / LevelManager.PPM) {
                enemy.b2body.setActive(true);
                enemy.setShot(true);
            }
        }

        creator.getTheRedBug().update(dt);

        for(Item item : hearts) {
            item.update(dt);
        }
        for(ProyectilHank item : proyectilesHank) {
            item.update(dt);
        }
        for(Item item : proyectiles) {
            item.update(dt);
        }
        //Gdx.app.log("Lenght",""+hearts.size);
        if(gamecam.position.x>119) {
            gamecam.position.x = 119.6f;
            if(gamecam.position.y > 3.6f) {
                gamecam.position.y -= 0.05f;
            }
            for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
                if(object.getProperties().containsKey("Boss"))
                    new Obstacules(this,object);
            }
            player.boss=true;
        }
        else {
            if (player.b2body.getPosition().x >= (LevelManager.V_WIDTH / 2 / LevelManager.PPM))
                gamecam.position.x = player.b2body.getPosition().x;

            if (player.b2body.getPosition().y >= (LevelManager.V_HEIGHT / 2 / LevelManager.PPM)
                    && player.b2body.getPosition().y <= ((LevelManager.V_HEIGHT + 360) / LevelManager.PPM))
                gamecam.position.y = player.b2body.getPosition().y;
        }

        gamecam.update();
        //Setting the camara to our map
        renderer.setView(gamecam);

    }

    private void manageBoss(float dt) {
        timerBoss += dt;
        if (timerBoss <= 4) {
            if (first && creator.getTheRedBug().b2body.getLinearVelocity().y == 0) {
                creator.getTheRedBug().b2body.applyLinearImpulse(new Vector2(-5f, 9f), creator.getTheRedBug().b2body.getWorldCenter(), true);
                first = false;
            } else if(creator.getTheRedBug().b2body.getLinearVelocity().y == 0){
                creator.getTheRedBug().b2body.applyLinearImpulse(new Vector2(5f, 9f), creator.getTheRedBug().b2body.getWorldCenter(), true);
                first = true;
            }
        }
        if (timerBoss >= 4) {
            startTime += dt;
            if(startTime >= 2){
                timerBoss = 0;
                startTime = 0;
            }
        }
    }

    private void handleInput(float dt) {

        if(player.currentState != Character.State.DEAD) {
            if(player.currentState != Character.State.ATTACKING) {
                if (player.currentState != Character.State.DAMAGED) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.DPAD_UP))
                    //&& player.b2body.getPosition().y < (Level1.V_HEIGHT + 500) / Level1.PPM
                    //&& (player.currentState == Character.State.RUNNING
                    //|| player.currentState == Character.State.STANDING)) {
                    {
                        player.b2body.applyLinearImpulse(new Vector2(0, 8f), player.b2body.getWorldCenter(), true);
                    }
                    if ((Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) || hud.getBtnRig())
                            && player.b2body.getLinearVelocity().x <= 3
                            && player.currentState != Character.State.DAMAGED
                            && !player.damaged
                            && player.currentState != Character.State.WIN){
                        player.b2body.applyLinearImpulse(new Vector2(0.5f, 0), player.b2body.getWorldCenter(), true);
                    }

                    if ((Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || hud.getBtnLef())
                            && player.b2body.getLinearVelocity().x >= -3
                            && player.currentState != Character.State.DAMAGED
                            && !player.damaged
                            && player.currentState != Character.State.WIN) {
                        player.b2body.applyLinearImpulse(new Vector2(-0.5f, 0), player.b2body.getWorldCenter(), true);
                    }
                    if (player.currentState == Character.State.RUNNING && !(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || hud.getBtnLef() || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) || hud.getBtnRig()))
                        if (player.b2body.getLinearVelocity().x > 0 || player.b2body.getLinearVelocity().x < 0) {
                            player.b2body.setLinearVelocity(0, 0);
                        }
                    //aqui es donde se tiene que poner el inicio de la animacion con algo asi como un timer.
                }
            }
        }

    }

    @Override
    public void render(float delta) {

        if(updateObjets) {
            update(delta);
        }
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
        for(Enemy enemy : creator.getMosquitos()) {
            enemy.draw(game.batch);
        }
        for(Enemy enemy : creator.getSlugs()) {
            enemy.draw(game.batch);
        }
        for(Enemy enemy : creator.getSpiders()) {
            enemy.draw(game.batch);
        }
        creator.getTheRedBug().draw(game.batch);
        for(Item item : hearts) {
            item.draw(game.batch);
        }
        for(ProyectilHank item : proyectilesHank){
            item.draw(game.batch);
        }
        for(Item item : proyectiles){
            item.draw(game.batch);
        }
        game.batch.end();
        hud.getStage().draw();

        if(gameOver()){
            game.getPantallaInicio().setScreen(new GameOverScreen(this));
            dispose();
        }else if(winScreen()){
            game.getPantallaInicio().setScreen(new WinnerScreen(this));
        }

    }

    private boolean gameOver(){
        return player.currentState == Character.State.DEAD && player.getStateTimer() > 3;
    }

    private boolean winScreen(){
        return player.currentState == Character.State.WIN && player.getStateTimer() > 3;
    }

    //region GETTERS & SETTERS

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

    public LevelManager getGame() {
        return game;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public Hud getHud() {
        return hud;
    }

    public Screen getScreen(){
        return this;
    }

    public int getLevel(){
        return level;
    }

    public String getEnemyType(){
        return enemyType;
    }

    public void setEnemyType(String enemy){
        this.enemyType = enemy;
    }

    public Array<Item> getHearts() {
        return hearts;
    }

    public Array<Item> getProyectiles() {
        return proyectiles;
    }

    public Array<ProyectilHank> getProyectilesHank() {
        return proyectilesHank;
    }

    public boolean isHank() {
        return isHank;
    }

    //endregion

    //region SCREEN METHODS
    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);

    }

    @Override
    public void pause() {
        if(getHud().isFirstPause()) {
            updateObjets = false;
            getHud().createPauseButtons();
            getHud().paused();
            getHud().setFirstPause(false);
        }
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

    //endregion



}



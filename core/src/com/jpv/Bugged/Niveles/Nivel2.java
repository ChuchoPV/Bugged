package com.jpv.Bugged.Niveles;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jpv.Bugged.Level1.Level1;
import com.jpv.Bugged.Level1.Scenes.Hud;
import com.jpv.Bugged.Level1.Sprites.Character;
import com.jpv.Bugged.Level1.Sprites.Items.Item;
import com.jpv.Bugged.Level1.Sprites.Items.ItemDef;
import com.jpv.Bugged.Level1.Tools.B2WorldCreator;
import com.jpv.Bugged.Level1.Tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingDeque;

public class Nivel2 extends Screen {
    //region VARIABLES
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

    private Stage stage;
    public boolean updateObjets;

    //endregion

    public Nivel2(Level1 game){
        atlas = new TextureAtlas("ATLAS_FINAL.pack");
        //Our game
        this.game = game;
        gamecam = new OrthographicCamera();
        //creat a FitViewport to maintain virtual aspect radio despite screen size
        gamePort = new FitViewport(Level1.V_WIDTH / Level1.PPM, Level1.V_HEIGHT / Level1.PPM, gamecam);
        //The map atributes
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("Mountain_Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Level1.PPM);
        //The camara set at the world
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() /2,0);
        //Setting the variables of our world
        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawBodies(false);
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

        updateObjets = true;
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    }
}

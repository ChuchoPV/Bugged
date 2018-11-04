package com.jpv.Bugged.Niveles;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jpv.Bugged.Level1.LevelManager;
import com.jpv.Bugged.Level1.Scenes.Hud;
import com.jpv.Bugged.Level1.Sprites.Character;
import com.jpv.Bugged.Level1.Sprites.Items.Item;
import com.jpv.Bugged.Level1.Sprites.Items.ItemDef;
import com.jpv.Bugged.Level1.Tools.B2WorldCreator;

import java.util.concurrent.LinkedBlockingDeque;

public class Nivel4 implements Screen {
    //Region Variables
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

    private Array<Item> items;
    private LinkedBlockingDeque<ItemDef> itemsToSpawn;

    private int timerBoss;
    private long startTime;
    private boolean first;

    private Stage stage;
    public boolean updateObjets;

    //endregion


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

package mx.itesm.Bugged.Niveles.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import mx.itesm.Bugged.Niveles.LevelManager;
import mx.itesm.Bugged.Niveles.Scenes.Hud;
import mx.itesm.Bugged.Niveles.Sprites.Character;
import mx.itesm.Bugged.Niveles.Sprites.Enemies.Enemy;
import mx.itesm.Bugged.Niveles.Sprites.Items.Heart;
import mx.itesm.Bugged.Niveles.Sprites.Items.Item;
import mx.itesm.Bugged.Niveles.Sprites.Items.ItemDef;
import mx.itesm.Bugged.Niveles.Sprites.Items.Proyectil;
import mx.itesm.Bugged.Niveles.Sprites.Items.ProyectilHank;
import mx.itesm.Bugged.Niveles.Tools.B2WorldCreator;
import mx.itesm.Bugged.Niveles.Tools.WorldContactListener;
import mx.itesm.Bugged.Niveles.Sprites.TileObjects.Obstacules;

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

    private float tiempo=0;
    private float timerBoss;
    private float startTimeBoss;
    //private float stopBossTimer;
    private boolean firstBossJump;

    //region Variables_TheKing
    private boolean first=true;
    private boolean secondb=true;
    private int second=0;
    private int secondrate=1;
    private boolean threeb=true;
    private int three=0;
    //endregion

    public boolean updateObjets;
    private String enemyType;
    private boolean isHank;

    private int level;
    private Music music;

    //endregion

    public PlayScreen(LevelManager game, int level){
        //./gradlew ios:createipa
        //./gradlew ios:simulatorios
        //Our game
        this.game = game;
        atlas = this.game.getManager().get("ATLAS_Final.pack", TextureAtlas.class);
        //Set the number of level
        this.level = level;
        gamecam = new OrthographicCamera();
        //creat a FitViewport to maintain virtual aspect radio despite screen size
        gamePort = new FitViewport(LevelManager.V_WIDTH / LevelManager.PPM, LevelManager.V_HEIGHT / LevelManager.PPM, gamecam);
         //The map atributes
        if(level == 1){
            map = game.getManager().get("City_Map.tmx");
            music = game.getManager().get("music/Scarf & Chocolates.mp3", Music.class);
            music.play();
            music.setLooping(true);
        }else if(level == 2){
            map = game.getManager().get("Subs_Map.tmx");
            music = game.getManager().get("music/Sinner.mp3", Music.class);
        }else if(level == 3){
            map = game.getManager().get("Mountain_Map.tmx");
            music = game.getManager().get("music/Piano Lesson.mp3", Music.class);
        }else if(level == 4){
            map = game.getManager().get("Cave_Map.tmx");
            music = game.getManager().get("music/Sofa Memorabilida (kumatora).mp3", Music.class);
        }else{
            map = game.getManager().get("Final.tmx");
            music = game.getManager().get("music/Coconut Water.mp3", Music.class);
        }

        if(game.getPantallaInicio().musicIsOn()){
            music.play();
            music.setLooping(true);
        }else{
            music.stop();
        }
        renderer = new OrthogonalTiledMapRenderer(map, 1 / LevelManager.PPM);
        //The camara set at the world
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() /2,0);
        //Setting the variables of our world
        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawBodies(false);//siguiendo instrucciones de chucho
        b2dr.SHAPE_STATIC.set(0,0,0,1);
        this.isHank = game.isHank();
        //Instance of out player
        player = new Character(this);
        creator = new B2WorldCreator(this);
        world.setContactListener(new WorldContactListener());
        hud = new Hud(this);
        timerBoss = 0;
        startTimeBoss = 0;
        //stopBossTimer = 0;
        firstBossJump = true;

        hearts = new Array<Item>();
        proyectiles = new Array<Item>();
        proyectilesHank = new Array<ProyectilHank>();
        heartsToSpawn = new LinkedBlockingDeque<ItemDef>();
        proyectilToSpawn = new LinkedBlockingDeque<ItemDef>();
        proyectilesHankToSpawn = new LinkedBlockingDeque<ItemDef>();

        updateObjets = true;
        enemyType = "";
        Gdx.input.setCatchBackKey(true);
    }

    //region SPAWN ITEMS
    private boolean fliped;
    public void spawnItem(ItemDef idef, boolean fliped) {
        this.fliped=fliped;
        if(idef.type == Heart.class){
            heartsToSpawn.add(idef);
        }
        else if(proyectilesHankToSpawn.isEmpty()) {
            if (idef.type == Proyectil.class) {
                proyectilToSpawn.add(idef);
            }
            if (idef.type == ProyectilHank.class) {
                proyectilesHankToSpawn.add(idef);
            }
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

        //region FINAL UPDATE
        if(this.level==5){
            int fase=this.order66();
            Vector2 kingpin=this.creator.getTheking().b2body.getPosition(),kingvel=this.creator.getTheking().b2body.getLinearVelocity();
            tiempo+=dt;
            switch (fase) {
                //region FASE1
                case (1):
                    System.out.println(tiempo);
                    float Bx=11.585f,Ax=1.221665f,Ay=3.928f;
                    if(kingvel.x==0 && tiempo>5){
                        first=!first;
                        tiempo=0;
                    }else {
                        if (first) {
                            this.getCreator().getTheking().b2body.setLinearVelocity(Bx - kingpin.x, Ay - kingpin.y);
                        } else {
                            this.getCreator().getTheking().b2body.setLinearVelocity(Ax - kingpin.x, Ay - kingpin.y);
                        }
                    }
                    break;
                    //endregion
                //region FASE2
                case(2):
                    float limit=1;
                    boolean velocityb=(kingvel.x<limit && kingvel.y<limit && kingvel.x>-limit && kingvel.y>-limit);
                    float By=8;
                    Bx=11.585f;Ax=1.221665f;Ay=3.928f;
                    switch (second){
                        case (0):
                            this.getCreator().getTheking().b2body.setLinearVelocity(Bx - kingpin.x, By - kingpin.y);
                            if(velocityb && secondb && tiempo>1){
                                secondrate=1;
                                second+=secondrate;
                                tiempo=0;
                                secondb=!secondb;
                            }
                            break;
                        case (1):
                            this.getCreator().getTheking().b2body.setLinearVelocity(Bx - kingpin.x, Ay - kingpin.y);
                            if(velocityb && !secondb && tiempo>.5){
                                second+=secondrate;
                                tiempo=0;
                                secondb=!secondb;
                            }
                            break;
                        case (2):
                            this.getCreator().getTheking().b2body.setLinearVelocity(Ax - kingpin.x, Ay - kingpin.y);
                            if(velocityb && secondb && tiempo>.5){
                                second+=secondrate;
                                tiempo=0;
                                secondb=!secondb;
                            }
                            break;
                        case (3):
                            this.getCreator().getTheking().b2body.setLinearVelocity(Ax - kingpin.x, By - kingpin.y);
                            if(velocityb && !secondb && tiempo>1){
                                secondrate=-1;
                                second+=secondrate;
                                tiempo=0;
                                secondb=!secondb;
                            }
                            break;
                    }
                    break;
                    //endregion
                //region FASE3
                case (3):
                    limit=1;
                    velocityb=(kingvel.x<limit && kingvel.y<limit && kingvel.x>-limit && kingvel.y>-limit);
                    Ay=3.5f;
                    By=5;
                    float Cy=6.8f;
                    Ax=1.221665f;
                    Bx=2.5f;
                    float Cx=10,Dx=11.585f;
                    switch (three){
                        case (0):
                            this.getCreator().getTheking().b2body.setLinearVelocity(Dx - kingpin.x, By - kingpin.y);
                            if(velocityb && threeb && tiempo>1){
                                tiempo=0;
                                threeb=!threeb;
                                three++;
                            }
                            break;
                        case (1):
                            this.getCreator().getTheking().b2body.setLinearVelocity(Cx - kingpin.x, Cy - kingpin.y);
                            if(velocityb && !threeb && tiempo>1){
                                tiempo=0;
                                threeb=!threeb;
                                three++;
                            }
                            break;
                        case (2):
                            this.getCreator().getTheking().b2body.setLinearVelocity(Bx - kingpin.x, Ay - kingpin.y);
                            if(velocityb && threeb && tiempo>1){
                                tiempo=0;
                                threeb=!threeb;
                                three++;
                            }
                            break;
                        case (3):
                            this.getCreator().getTheking().b2body.setLinearVelocity(Ax - kingpin.x, By - kingpin.y);
                            if(velocityb && !threeb && tiempo>1){
                                tiempo=0;
                                threeb=!threeb;
                                three++;
                            }
                            break;
                        case (4):
                            this.getCreator().getTheking().b2body.setLinearVelocity(Bx - kingpin.x, Cy - kingpin.y);
                            if(velocityb && threeb && tiempo>1){
                                tiempo=0;
                                threeb=!threeb;
                                three++;
                            }
                            break;
                        case (5):
                            this.getCreator().getTheking().b2body.setLinearVelocity(Cx - kingpin.x, Ay - kingpin.y);
                            if(velocityb && !threeb && tiempo>1){
                                tiempo=0;
                                threeb=!threeb;
                                three=0;
                            }
                            break;
                    }
                    break;
                    //endregion
                case (-1):
                    this.getCreator().getTheking().b2body.setLinearVelocity(0,0);
                    break;
            }
        }
        //endregion
        //region OBJECT_UPDATES
        if(player.boss) {
            for(Enemy enemy : creator.getMosquitos()) {
                enemy.destroy();
            }
            if(level == 1) {
                manageBoss(dt);
            }
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

        if(level == 1) {
            creator.getTheRedBug().update(dt);
        }if(level == 5) {
            creator.getTheking().update(dt);
        }

        for(Item item : hearts) {
            item.update(dt);
        }
        for(ProyectilHank item : proyectilesHank) {
            item.update(dt);
        }
        for(Item item : proyectiles) {
            item.update(dt);
        }
        //endregion
        //region THE_RED_BUG_MANAGER
        if(gamecam.position.x>119) {
            if(this.level != 1 && gamecam.position.x>119) {
                player.currentState = Character.State.WIN;
            }else {
                gamecam.position.x = 119.6f;
                if (gamecam.position.y > 3.6f) {
                    gamecam.position.y -= 0.05f;
                }
                for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
                    if (object.getProperties().containsKey("Boss"))
                        new Obstacules(this, object);
                }
                player.boss = true;
            }
        } else {
            if(this.level != 5) {
                if (player.b2body.getPosition().x >= (LevelManager.V_WIDTH / 2 / LevelManager.PPM)) {
                    gamecam.position.x = player.b2body.getPosition().x;
                }
                if (player.b2body.getPosition().y >= (LevelManager.V_HEIGHT / 2 / LevelManager.PPM)
                        && player.b2body.getPosition().y <= ((LevelManager.V_HEIGHT + 360) / LevelManager.PPM)) {
                    gamecam.position.y = player.b2body.getPosition().y;
                }
            }
        }

        //endregion
        gamecam.update();
        //Setting the camara to our map
        renderer.setView(gamecam);

    }

    public int order66() {
        int live=this.getCreator().getTheking().getLives();
        int res;
        if(live>7){
            res=1;
        }
        else if(live>3 && live<8){
            res=2;
        }
        else if(live>=0 && live<4){
            res=3;
        }
        else{
            res=-1;
        }
        return res;
    }

    private void manageBoss(float dt) {
        startTimeBoss += dt;
        timerBoss += dt;
        if(startTimeBoss>0.5f) {
            if (timerBoss < 4) {
                if (creator.getTheRedBug().b2body.getLinearVelocity().y == 0 && creator.getTheRedBug().b2body.getPosition().y < 3.23 && creator.getTheRedBug().b2body.getLinearVelocity().x == 0) {
                    if (firstBossJump) {
                        creator.getTheRedBug().b2body.applyLinearImpulse(new Vector2(-5f, 9f), creator.getTheRedBug().b2body.getWorldCenter(), true);
                        this.firstBossJump = false;
                    } else {
                        creator.getTheRedBug().b2body.applyLinearImpulse(new Vector2(5f, 9f), creator.getTheRedBug().b2body.getWorldCenter(), true);
                        this.firstBossJump = true;
                    }
                }
            } else if (timerBoss > 6) {
                this.timerBoss = 0;
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
                        player.b2body.applyLinearImpulse(new Vector2(0, 7f), player.b2body.getWorldCenter(), true);
                    }
                    if ((Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) || hud.getBtnRig())
                            && player.b2body.getLinearVelocity().x <= 5
                            && player.currentState != Character.State.DAMAGED
                            && !player.damaged
                            && player.currentState != Character.State.WIN){
                        player.b2body.applyLinearImpulse(new Vector2(0.5f, 0), player.b2body.getWorldCenter(), true);
                    }

                    if ((Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || hud.getBtnLef())
                            && player.b2body.getLinearVelocity().x >= -5
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
        game.getManager().update();
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
        if(level == 1) {
            creator.getTheRedBug().draw(game.batch);
        }if(level == 5) {
            creator.getTheking().draw(game.batch);
        }
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
            if(game.getPantallaInicio().musicIsOn()) {
               music.stop();
            }
            game.getPantallaInicio().setScreen(new GameOverScreen(this));
            dispose();
        }else if(winScreen()){
            if(game.getPantallaInicio().musicIsOn()) {
               music.stop();
            }
            game.getPantallaInicio().setScreen(new WinnerScreen(this));
            dispose();
        }

    }

    private boolean gameOver() {
        return player.currentState == Character.State.DEAD && player.getStateTimer() > 3;
    }

    private boolean winScreen(){
        //if(game.getPantallaInicio().musicIsOn()) {
        //    music.stop();
        //}
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

    public Music getMusic() {
        return music;
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
        //game.getManager().dispose();

    }

    //endregion



}



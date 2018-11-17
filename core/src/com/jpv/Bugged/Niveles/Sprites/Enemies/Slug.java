package com.jpv.Bugged.Niveles.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.jpv.Bugged.Niveles.LevelManager;
import com.jpv.Bugged.Niveles.Screens.PlayScreen;
import com.jpv.Bugged.Niveles.Sprites.Items.Heart;
import com.jpv.Bugged.Niveles.Sprites.Items.ItemDef;
import com.jpv.Bugged.Niveles.Sprites.Items.Proyectil;

public class Slug extends Enemy{
    private boolean setToDestroy;
    private boolean destroyed;
    private boolean damagedB;
    private boolean first;

    private float stateTimer;
    private Animation idle;
    private Animation kill;
    private Animation attack;
    private Animation damage;

    private boolean shot;
    private float shotTimer;
    private boolean isFlip;

    public Slug(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y,object);
        b2body.setActive(true);
        TextureAtlas atlas = screen.getAtlas();
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(atlas.findRegion("slug_idle"), i * 144, 0, 144, 100));

        }idle = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(atlas.findRegion("slug_damage"), i * 144, 0, 144, 100));
        }damage = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 9; i++) {
            frames.add(new TextureRegion(atlas.findRegion("slug_dead"), i * 144, 0, 144, 100));
        }kill = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(atlas.findRegion("slug_atk"), i * 144, 0, 144, 100));
        }attack = new Animation<TextureRegion>(0.1f, frames);

        stateTimer = 0;
        damaged = 0;
        damagedB = false;
        setToDestroy = false;
        destroyed = false;
        first = true;
        shot = false;
        shotTimer = 0;
        isFlip = false;
        setBounds(getX(),getY(), 144 / LevelManager.PPM,100 / LevelManager.PPM);

    }

    public void update(float dt){
        //Esta es la parte que funciona
        stateTimer += dt;
        shotTimer += dt;

        if(this.playerDistance()<7){
            this.setShot(true);
        }
        else{
            this.setShot(false);
        }
        if(shot && shotTimer >= 2 && !damagedB){
            screen.setEnemyType("slug");
            /*screen.spawnItem(new ItemDef(new Vector2(b2body.getPosition().x, b2body.getPosition().y),
                    Proyectil.class));*/
            screen.spawnItem(new ItemDef(this.b2body.getPosition(),
                    Proyectil.class), this.isFlip());
            shotTimer = 0;
        }

        flip=super.toFlip();
        TextureRegion region;

        if(damagedB && !setToDestroy && !destroyed){
            //stateTimer = 0;
            region=(TextureRegion) damage.getKeyFrame(stateTimer);
            if(flip) {
                if (!region.isFlipX())
                    region.flip(true, false);
            }else{
                if(region.isFlipX())
                    region.flip(true,false);
            }
            setRegion(region);
            this.b2body.setLinearVelocity(0,0);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            if(damage.isAnimationFinished(stateTimer)) {
                damagedB = false;
            }
        }else if(setToDestroy && !destroyed){
            if(first) {
                world.destroyBody(b2body);
                first = false;
            }
            setRegion((TextureRegion) kill.getKeyFrame(stateTimer));
            if(kill.isAnimationFinished(stateTimer)) {
                destroyed = true;
                stateTimer = 0;
            }
        }else if(!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            region=(TextureRegion) idle.getKeyFrame(stateTimer,true);
            if(flip) {
                if (!region.isFlipX()) {
                    region.flip(true, false);
                    isFlip = true;
                }
            }else{
                if(region.isFlipX()) {
                    region.flip(true, false);
                    isFlip = false;
                }
            }
            setRegion(region);
            damagedB = false;
            if(idle.isAnimationFinished(stateTimer)){
                stateTimer = 0;
            }
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(57/ LevelManager.PPM, 40 / LevelManager.PPM);
        fdef.filter.categoryBits = LevelManager.ENEMY_BIT;
        fdef.filter.maskBits = LevelManager.GROUND_BIT
                | LevelManager.PLATAFORM_BIT
                | LevelManager.OBSTACULE_BIT
                | LevelManager.ENEMY_BIT
                | LevelManager.OBJECT_BIT
                | LevelManager.CHARACTER_BIT;


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Create collider hear
        PolygonShape collider = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-57 , -40).scl(1 / LevelManager.PPM);
        vertice[1] = new Vector2(57 , -40).scl(1 / LevelManager.PPM);
        vertice[2] = new Vector2(-57     , 40).scl(1 / LevelManager.PPM);
        vertice[3] = new Vector2(57 , 40).scl(1 / LevelManager.PPM);
        collider.set(vertice);

        fdef.shape = collider;
        fdef.filter.categoryBits = LevelManager.ENEMY_COLLIDER_BIT;
        b2body.createFixture(fdef).setUserData(this);

        //Create collider hear
        PolygonShape colliderDisp = new PolygonShape();
        Vector2[] vertice2 = new Vector2[4];
        vertice2[0] = new Vector2(-57 , -40).scl(1 / LevelManager.PPM);
        vertice2[1] = new Vector2(57 , -40).scl(1 / LevelManager.PPM);
        vertice2[2] = new Vector2(-57     , 40).scl(1 / LevelManager.PPM);
        vertice2[3] = new Vector2(57 , 40).scl(1 / LevelManager.PPM);
        colliderDisp.set(vertice);

        fdef.shape = colliderDisp;
        fdef.filter.categoryBits = LevelManager.ENEMY_COLLIDER_BIT;
        b2body.createFixture(fdef).setUserData(this);


    }

    public void draw(Batch batch){
        if(!destroyed){
            super.draw(batch);
        }
    }
    public void setShot(boolean shot){
        this.shot = shot;
    }
    private boolean isFlip() {
        return isFlip;
    }

    @Override
    public void onHeadHit() {
        if(!damagedB) {
            if (damaged == 2) {
                if (object.getProperties().containsKey("Heart")) {
                    screen.spawnItem(new ItemDef(new Vector2(b2body.getPosition().x, b2body.getPosition().y),
                            Heart.class), false);
                }
                setShot(false);
                setToDestroy = true;

            } else {
                damagedB = true;
                damaged++;

            }
        }
    }

    //Metodo prueba para saber si el jugador esta
    // cerca del enemigo y sabe si este deberia estar disparando
    private double playerDistance(){
        double ejey=Math.pow((screen.getPlayer().b2body.getPosition().y-this.b2body.getPosition().y),2);
        double ejex=Math.pow((screen.getPlayer().b2body.getPosition().x-this.b2body.getPosition().x),2);
        double distance=Math.sqrt(ejex+ejey);
        return distance;

    }

}

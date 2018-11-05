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

public class Spider extends Enemy{
    private boolean setToDestroy;
    private boolean destroyed;
    private boolean damagedB;
    private boolean first;

    private float stateTimer;
    private Animation idle;
    private Animation kill;
    private Animation attack;
    private TextureRegion jump;
    private TextureRegion fall;
    private Animation damage;

    private boolean shot;
    private float shotTimer;

    public Spider(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y,object);
        TextureAtlas atlas = screen.getAtlas();
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(atlas.findRegion("spider_idle"), i * 133, 0, 133, 120));

        }idle = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(atlas.findRegion("spider_damage"), i * 133, 0, 133, 120));
        }damage = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 9; i++) {
            frames.add(new TextureRegion(atlas.findRegion("spider_dead"), i * 133, 0, 133, 120));
        }kill = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(atlas.findRegion("spider_atk"), i * 133, 0, 133, 120));
        }attack = new Animation<TextureRegion>(0.1f, frames);

        jump = new TextureRegion(atlas.findRegion("spider_jump"),266,0,133,120);
        fall = new TextureRegion(atlas.findRegion("spider_jump"),399,0,133,120);

        stateTimer = 0;
        damaged = 0;
        damagedB = false;
        setToDestroy = false;
        destroyed = false;
        first = true;
        shot = false;
        shotTimer = 0;
        setBounds(getX(),getY(), 133 / LevelManager.PPM,120 / LevelManager.PPM);

    }

    public void update(float dt){
        //Esta es la parte que funciona
        stateTimer += dt;
        shotTimer += dt;
        if(shot && shotTimer >= 2){
            screen.setEnemyType("spider");
            screen.spawnItem(new ItemDef(new Vector2(b2body.getPosition().x, b2body.getPosition().y),
                    Proyectil.class));
            shotTimer = 0;
        }

        if(damagedB && !setToDestroy && !destroyed){
            //stateTimer = 0;
            setRegion((TextureRegion) damage.getKeyFrame(stateTimer));
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
            setRegion((TextureRegion) idle.getKeyFrame(stateTimer,true));
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
                | LevelManager.CHARACTER_BIT
                | LevelManager.CHARACTER_ARMA_BIT;


        fdef.shape = shape;
        b2body.createFixture(fdef);

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


    }

    public void draw(Batch batch){
        if(!destroyed){
            super.draw(batch);
        }
    }
    public void setShot(boolean shot){
        this.shot = shot;
    }
    public boolean getShot() {
        return shot;
    }

    @Override
    public void onHeadHit() {
        if(!damagedB) {
            if (damaged == 2) {
                if (object.getProperties().containsKey("Heart")) {
                    screen.spawnItem(new ItemDef(new Vector2(b2body.getPosition().x, b2body.getPosition().y),
                            Heart.class));
                }
                setShot(false);
                setToDestroy = true;

            } else {
                damagedB = true;
                damaged++;

            }
        }
    }



}

package com.jpv.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.jpv.Level1;
import com.jpv.Screens.PlayScreen;
import com.badlogic.gdx.utils.Array;
import com.jpv.Sprites.Items.Heart;
import com.jpv.Sprites.Items.ItemDef;


public class Mosquito extends Enemy {
    private boolean setToDestroy;
    private boolean destroyed;
    private boolean damagedB;
    private int damaged;
    private int move;

    private float stateTimer;
    private Animation idle;
    private TextureRegion kill;
    private TextureRegion damage;
    private Array<TextureRegion> frames;

    public Mosquito(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y,object);

        frames = new Array<TextureRegion>();
        for(int i = 0; i < 8; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("mosquito_idle"),i * 160, 0, 160,160));
        idle = new Animation(0.1f,frames);

        kill = new TextureRegion(screen.getAtlas().findRegion("mosquito_damage"), 160,0,160,160);
        damage = new TextureRegion(screen.getAtlas().findRegion("mosquito_damage"), 320,0,160,160);

        stateTimer = 0;
        damaged = 0;
        move = 0;
        damagedB = false;
        setToDestroy = false;
        destroyed = false;
        setBounds(getX(),getY(), 160 / Level1.PPM,160 / Level1.PPM);

    }

    public void update(float dt){
        stateTimer += dt;
        move ++;

        if(damagedB){
            setRegion(damage);
            damagedB = false;
        }
        else if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(kill);
            stateTimer = 0;
        }else if(!destroyed) {
            damagedB = false;
            b2body.setLinearVelocity(velocity);
            if (move > 50){
                reverseVelocity(false, true);
                move = 0;
            }
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 3);
            setRegion((TextureRegion) idle.getKeyFrame(stateTimer,true));
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
        //PolygonShape shape = new PolygonShape();
        //shape.setAsBox(20,20);
        shape.setAsBox(60/ Level1.PPM, 60 / Level1.PPM);
        //shape.setRadius(20 / Level1.PPM);
        fdef.filter.categoryBits = Level1.ENEMY_BIT;
        fdef.filter.maskBits = Level1.GROUND_BIT
                | Level1.PLATAFORM_BIT
                | Level1.OBSTACULE_BIT
                | Level1.ENEMY_BIT
                | Level1.OBJECT_BIT
                | Level1.CHARACTER_BIT
                | Level1.CHARACTER_ARMA_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        //Create collider hear
        PolygonShape collider = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-60 , -60).scl(1 / Level1.PPM);
        vertice[1] = new Vector2(60 , -60).scl(1 / Level1.PPM);
        vertice[2] = new Vector2(-60     , 60).scl(1 / Level1.PPM);
        vertice[3] = new Vector2(60 , 60).scl(1 / Level1.PPM);
        collider.set(vertice);

        fdef.shape = collider;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = Level1.ENEMY_COLLIDER_BIT;
        b2body.createFixture(fdef).setUserData(this);


    }

    public void draw(Batch batch){
        if(!destroyed || stateTimer < 1){
            super.draw(batch);
        }
    }

    @Override
    public void onHeadHit() {
        if(damaged == 2) {
            if(object.getProperties().containsKey("Heart")){
                screen.spawnItem(new ItemDef(new Vector2(b2body.getPosition().x, b2body.getPosition().y ),
                Heart.class));
            }
            setToDestroy = true;

        }else{
            damagedB = true;
            damaged++;

        }
    }
}

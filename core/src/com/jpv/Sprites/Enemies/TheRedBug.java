package com.jpv.Sprites.Enemies;

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
import com.jpv.Level1;
import com.jpv.Screens.PlayScreen;

public class TheRedBug extends Enemy{
    private boolean setToDestroy;
    private boolean destroyed;
    private boolean damagedB;
    private int damaged;
    private int move;

    private float stateTimer;
    private Animation idle;
    private Animation kill;
    private Animation damage;
    private Animation jump;
    private Array<TextureRegion> frames;

    public TheRedBug(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y, object);

        TextureAtlas atlas = new TextureAtlas("Enemy.pack");
        frames = new Array<TextureRegion>();

        for(int i = 0; i < 4; i++)
            frames.add(new TextureRegion(atlas.findRegion("RedBug_idle"),i * 320, 0, 320,230));
        idle = new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for(int i = 0; i < 4; i++)
            frames.add(new TextureRegion(atlas.findRegion("RedBug_Jump"),i * 320, 0, 320,230));
        jump = new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for(int i = 0; i < 4; i++)
            frames.add(new TextureRegion(atlas.findRegion("RedBug_damage"),i * 320, 0, 320,230));
        damage = new Animation<TextureRegion>(0.2f,frames);

        for(int i = 0; i < 10; i++)
            frames.add(new TextureRegion(atlas.findRegion("RedBug_dead"),i * 320, 0, 320,230));
        kill = new Animation<TextureRegion>(0.1f,frames);

        stateTimer = 0;
        damaged = 0;
        move = 0;
        damagedB = false;
        setToDestroy = false;
        destroyed = false;
        setBounds(getX(),getY(), 320 / Level1.PPM,230 / Level1.PPM);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(60/ Level1.PPM, 60 / Level1.PPM);
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
        fdef.restitution = 1f;
        fdef.filter.categoryBits = Level1.ENEMY_COLLIDER_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        if(!destroyed || stateTimer < 1){
            super.draw(batch);
        }
    }

    @Override
    public void update(float dt) {
        stateTimer += dt;

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 3);
        setRegion((TextureRegion) idle.getKeyFrame(stateTimer,true));
        damagedB = false;
        b2body.setLinearVelocity(velocity);
        if(idle.isAnimationFinished(stateTimer)){
            stateTimer = 0;
        }

    }

    @Override
    public void onHeadHit() {

    }
}

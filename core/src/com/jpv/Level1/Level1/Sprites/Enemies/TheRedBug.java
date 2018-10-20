package com.jpv.Level1.Level1.Sprites.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.jpv.Level1.Level1.Level1;
import com.jpv.Level1.Level1.Screens.PlayScreen;


public class TheRedBug extends Enemy {
    //region VARIABLES
    private boolean setToDestroy;
    private boolean destroyed;
    private boolean damagedB;
    private boolean isRight;
    private int damaged;

    private float stateTimer;
    private Animation idle;
    private Animation kill;
    private Animation damage;
    private TextureRegion jump;
    private TextureRegion fall;
    private boolean first;
    //endregion

    public TheRedBug(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y, object);
        TextureAtlas atlas = screen.getAtlas();
        Array<TextureRegion> frames = new Array<TextureRegion>();

        TextureRegion temp = null;
        for(int i = 0; i < 4; i++) {
            temp = new TextureRegion(atlas.findRegion("RedBug_idle"), i * 320, 0, 320, 230);
            temp.flip(true, false);
            frames.add(temp);
        }
        idle = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        jump = new TextureRegion(atlas.findRegion("RadBug_Jump"), 320,0,320,230);
        jump.flip(true,false);
        fall = new TextureRegion(atlas.findRegion("RadBug_Jump"), 640,0,320,230);
        fall.flip(true,false);

        for(int i = 0; i < 4; i++)
            frames.add(new TextureRegion(atlas.findRegion("RedBug_damage"),i * 320, 0, 320,230));
        damage = new Animation<TextureRegion>(0.2f, frames);

        for(int i = 0; i < 10; i++)
            frames.add(new TextureRegion(atlas.findRegion("RedBug_dead"),i * 320, 0, 320,230));
        kill = new Animation<TextureRegion>(0.1f, frames);

        stateTimer = 0;
        damaged = 0;
        damagedB = false;
        setToDestroy = false;
        destroyed = false;
        isRight = true;
        first = true;
        setBounds(getX(),getY(), 500 / Level1.PPM,320 / Level1.PPM); //320 ,230
        b2body.setActive(true);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(240/ Level1.PPM, 160 / Level1.PPM); //160,110
        fdef.filter.categoryBits = Level1.BOSS_BIT;
        fdef.filter.maskBits = Level1.GROUND_BIT
                | Level1.CHARACTER_BIT
                | Level1.CHARACTER_ARMA_BIT
                | Level1.OBSTACULE_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape pies = new EdgeShape();
        pies.set(new Vector2(140 / Level1.PPM, -160 / Level1.PPM), new Vector2(-140 / Level1.PPM, -160 / Level1.PPM));
        fdef.shape = pies;
        fdef.filter.categoryBits = Level1.BOSS_PIES_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape collider = new EdgeShape();
        collider.set(new Vector2(-240 / Level1.PPM, -140 / Level1.PPM), new Vector2(-240 / Level1.PPM, 140 / Level1.PPM));
        fdef.shape = collider;
        fdef.filter.categoryBits = Level1.BOSS_COLLIDER_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape collider2 = new EdgeShape();
        collider2.set(new Vector2(240 / Level1.PPM, -140 / Level1.PPM), new Vector2(240 / Level1.PPM, 140 / Level1.PPM));
        fdef.shape = collider2;
        fdef.filter.categoryBits = Level1.BOSS_COLLIDER_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if(!destroyed /*|| stateTimer < 1*/){
            super.draw(batch);
        }
        else{
            world.destroyBody(this.b2body);
        }
    }

    @Override
    public void update(float dt) {
        stateTimer += dt;
        TextureRegion region = null;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        //DAÑADO
        if(damagedB && !setToDestroy && !destroyed){
            setRegion((TextureRegion) damage.getKeyFrame(stateTimer));
            //setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 3);
            if(damage.isAnimationFinished(stateTimer)) {
                damagedB = false;
                Gdx.app.log("DAÑO", "TERMINA Dañado... *********" + stateTimer);
            }
            return;
        }
        //DESTRUYENDOSE
        if(setToDestroy && !destroyed){
            if(first){
                //world.destroyBody(b2body);
                first = false;
            }
            setRegion((TextureRegion) kill.getKeyFrame(stateTimer));
            if(kill.isAnimationFinished(stateTimer)) {
                destroyed = true;
                stateTimer = 0;
            }
            return;
        //SALTANDO
        }else if(b2body.getLinearVelocity().y > 0 ) {
            region = jump;
            if (screen.getPlayer().b2body.getPosition().x > b2body.getPosition().x) {
                if (region.isFlipX())
                    region.flip(true, false);
                isRight = false;
            }else if (screen.getPlayer().b2body.getPosition().x < b2body.getPosition().x && !isRight) {
                if (!region.isFlipX())
                    region.flip(true, false);
                isRight = false;
            }
            setRegion(region);
        //CAYENDO
        }else if(b2body.getLinearVelocity().y < 0 ){
            region = fall;
            if (screen.getPlayer().b2body.getPosition().x > b2body.getPosition().x){
                if (region.isFlipX())
                    region.flip(true, false);
                isRight = false;
            }else if (screen.getPlayer().b2body.getPosition().x < b2body.getPosition().x && !isRight) {
                if (!region.isFlipX())
                    region.flip(true, false);
                isRight = false;
            }
            setRegion(region);

        }else{
        //SI NADA DE LO DEMAS ESTA PASANDO, Y NO ESTOY MUERTO ESTOY QUIETO (IDLE)
        if(!destroyed){
            region = (TextureRegion) idle.getKeyFrame(stateTimer,true);
            if (screen.getPlayer().b2body.getPosition().x > b2body.getPosition().x){
                if (region.isFlipX())
                    region.flip(true, false);
                isRight = false;
            }else if (screen.getPlayer().b2body.getPosition().x < b2body.getPosition().x && !isRight) {
                if (!region.isFlipX())
                    region.flip(true, false);
                isRight = false;
            }
            b2body.setLinearVelocity(new Vector2(0f,0f));
            setRegion(region);
        }
        }

    }

    @Override
    public void onHeadHit() {
        if (!damagedB) {
            if (damaged == 3) {
                Gdx.app.log("HIT", "INICIA LA DESTRUCCION: " + damaged);
                setToDestroy = true;
                //screen.getPlayer().win = true;
                damagedB = true;
                stateTimer = 0;
            }
            else {
                stateTimer = 0;
                damagedB = true;
                damaged++;
                Gdx.app.log("HIT", "Pega: " + damaged);
            }
        }
    }
}
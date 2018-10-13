package com.jpv.Sprites.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private boolean isRight;
    private int damaged;

    private float stateTimer;
    private Animation idle;
    private Animation kill;
    private Animation damage;
    private TextureRegion jump;
    private TextureRegion fall;

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
    }

    public void draw(Batch batch){
        if(!destroyed || stateTimer < 1){
            super.draw(batch);
        }
    }

    @Override
    public void update(float dt) {
        //199 boss      //189.64 Hank   //Uno, dos, tres quieto y después de nuevo
        //Gdx.app.log("Posición",""+screen.getPlayer().b2body.getPosition().x);

        stateTimer += dt;
        TextureRegion region;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        if(b2body.getLinearVelocity().y > 0 ) {
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
        }else {
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

    @Override
    public void onHeadHit() {

    }
}

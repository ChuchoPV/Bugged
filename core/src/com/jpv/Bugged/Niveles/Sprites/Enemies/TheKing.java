package com.jpv.Bugged.Niveles.Sprites.Enemies;

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
import com.jpv.Bugged.Niveles.LevelManager;
import com.jpv.Bugged.Niveles.Screens.PlayScreen;

public class TheKing extends Enemy {
    //region VARIABLES
    private boolean setToDestroy;
    private boolean destroyed;
    private boolean damagedB;
    private int damaged;
    private boolean flip;

    private float stateTimer;

    private Animation idle;
    private Animation kill;
    private Animation damage;
    private Animation idle2;
    private Animation damage2;
    private Animation idle3;
    private Animation damage3;

    private boolean first;
    //endregion

    public TheKing(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y, object);
        TextureAtlas atlas = screen.getGame().getManager().get("ATLAS_Final_2.pack");
        Array<TextureRegion> frames = new Array<TextureRegion>();

        //region FASE1
        TextureRegion temp;
        for(int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(atlas.findRegion("king_1_idle"), i * 320, 0, 320, 320));
        }
        idle = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(atlas.findRegion("king_1_damage"), i * 320, 0, 320, 320));
        }
        damage = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();

        //endregion
        //region FASE2
        for(int i = 0; i < 8; i++) {
            temp = new TextureRegion(atlas.findRegion("king_2_idle"), i * 320, 0, 320, 320);
            temp.flip(true, false);
            frames.add(temp);
        }
        idle2 = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 4; i++) {
            temp = new TextureRegion(atlas.findRegion("king_2_damage"), i * 320, 0, 320, 320);
            temp.flip(true, false);
            frames.add(temp);
        }
        damage2 = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();

        //endregion
        //region FASE3
        for(int i = 0; i < 8; i++) {
            temp = new TextureRegion(atlas.findRegion("king_3_idle"), i * 320, 0, 320, 320);
            temp.flip(true, false);
            frames.add(temp);
        }
        idle3 = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 4; i++) {
            temp = new TextureRegion(atlas.findRegion("king_3_damage"), i * 320, 0, 320, 320);
            temp.flip(true, false);
            frames.add(temp);
        }
        damage3 = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();
        //endregion

        for(int i = 0; i < 11; i++) {
             frames.add(new TextureRegion(atlas.findRegion("king_dead"), i * 320, 0, 320, 320));
        }
        kill = new Animation<TextureRegion>(0.1f, frames);

        stateTimer = 0;
        damaged = 0;
        damagedB = false;
        setToDestroy = false;
        destroyed = false;
        first = true;
        this.b2body.setGravityScale(0);
        setBounds(getX(),getY(), 320 / LevelManager.PPM,320 / LevelManager.PPM); //320 ,230
        b2body.setActive(true);
    }

    public void draw(Batch batch){
        if(!destroyed){
            super.draw(batch);
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
        shape.setAsBox(150/ LevelManager.PPM, 150 / LevelManager.PPM); //160,110
        fdef.filter.categoryBits = LevelManager.BOSS_BIT;
        fdef.filter.maskBits = LevelManager.GROUND_BIT
                | LevelManager.CHARACTER_BIT
                | LevelManager.CHARACTER_ARMA_BIT
                | LevelManager.OBSTACULE_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //region COLLIDERS

        EdgeShape collider = new EdgeShape();
        collider.set(new Vector2(-150 / LevelManager.PPM, -140 / LevelManager.PPM), new Vector2(-150 / LevelManager.PPM, 140 / LevelManager.PPM));
        fdef.shape = collider;
        fdef.filter.categoryBits = LevelManager.BOSS_COLLIDER_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape collider2 = new EdgeShape();
        collider2.set(new Vector2(150 / LevelManager.PPM, -140 / LevelManager.PPM), new Vector2(150 / LevelManager.PPM, 140 / LevelManager.PPM));
        fdef.shape = collider2;
        fdef.filter.categoryBits = LevelManager.BOSS_COLLIDER_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        //endregion

    }

    @Override
    public void update(float dt) {
        stateTimer += dt;
        TextureRegion region;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        //DAÑADO
        if(damagedB && !setToDestroy && !destroyed){
            if(screen.order66()==1){
                region = (TextureRegion) damage.getKeyFrame(stateTimer);
            }
            else if(screen.order66()==2){
                region = (TextureRegion) damage2.getKeyFrame(stateTimer);
            }
            else{
                region = (TextureRegion) damage3.getKeyFrame(stateTimer);
            }
            if(damage.isAnimationFinished(stateTimer)) {
                damagedB = false;
            }
            setRegion(region);
            return;
        }
        //DESTRUYENDOSE
        else if(setToDestroy && !destroyed) {
            if (first) {
                world.destroyBody(this.b2body);
                first = false;
            }
            this.b2body.setGravityScale(0);
            this.b2body.setLinearVelocity(0, 0);
            setRegion((TextureRegion) kill.getKeyFrame(stateTimer));
            if (kill.isAnimationFinished(stateTimer)) {
                destroyed = true;
                stateTimer = 0;
            }
        }else{
            //SI NADA DE LO DEMAS ESTA PASANDO, Y NO ESTOY MUERTO ESTOY QUIETO (IDLE)
            if(!destroyed){
                if(screen.order66()==1){
                    region = (TextureRegion) idle.getKeyFrame(stateTimer,true);
                }
                else if(screen.order66()==2){
                    region = (TextureRegion) idle2.getKeyFrame(stateTimer,true);
                }
                else{
                    region = (TextureRegion) idle3.getKeyFrame(stateTimer,true);
                }
                if(!super.toFlip()){
                //if (screen.getPlayer().b2body.getPosition().x > b2body.getPosition().x){
                    if (region.isFlipX())
                        region.flip(true, false);
                }//else if (screen.getPlayer().b2body.getPosition().x < b2body.getPosition().x && !isRight) {
                else if(super.toFlip()){
                    if (!region.isFlipX())
                        region.flip(true, false);
                }
                setRegion(region);
            }
        }

    }

    @Override
    public void onHeadHit() {
        if (!damagedB) {
            if (damaged == 9) {
                setToDestroy = true;
                screen.getPlayer().win = true;
                damagedB = true;
                stateTimer = 0;
            }
            else {
                stateTimer = 0;
                damagedB = true;
                damaged++;
            }
        }

    }

    @Override
    public void setShot(boolean shot) {

    }

    /**
     * Metodo para poder saber cuantas vidas se tienen
     * @return lives el numero de vidas
     */
    public int getLives(){
        int lives = 9;
        return lives -this.damaged;
    }
}

package com.jpv.Bugged.Niveles.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.jpv.Bugged.Niveles.LevelManager;
import com.jpv.Bugged.Niveles.Screens.PlayScreen;
import com.jpv.Bugged.Niveles.Sprites.Character;

public class Proyectil extends Item{
    private String enemy;

    public Proyectil(PlayScreen screen, float x, float y, String enemy) {
        super(screen, x, y, enemy);
        this.enemy = enemy;
        b2body.setGravityScale(0);
        if(enemy.equals("spider")) {
            setRegion(new TextureRegion(screen.getAtlas().findRegion("spider_prjct"), 0, 0, 50, 70));
        }else if(enemy.equals("slug")) {
            setRegion(new TextureRegion(screen.getAtlas().findRegion("slug_prjct"), 0, 0, 50, 70));
        }
        else {
            TextureRegion region = new TextureRegion(screen.getAtlas().findRegion("projectile_salt"), 0, 0, 50, 70); //Hank_Shoot
            region.flip(true,false);
            setRegion(region);
        }
    }
    
    @Override
    public void defineItem() {
        if(super.enemy.equals("spider") || super.enemy.equals("slug")){
            BodyDef bdef = new BodyDef();
            bdef.position.set(getX(),getY());
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bdef);

            FixtureDef fdef = new FixtureDef();
            CircleShape shape = new CircleShape();
            shape.setRadius(10 / LevelManager.PPM);
            fdef.filter.categoryBits = LevelManager.ENEMY_PROYECT;
            fdef.filter.maskBits = LevelManager.CHARACTER_BIT
                    | LevelManager.OBJECT_BIT
                    | LevelManager.GROUND_BIT
                    | LevelManager.PLATAFORM_BIT
                    | LevelManager.OBSTACULE_BIT;

            fdef.shape = shape;
            b2body.createFixture(fdef).setUserData(this);
        }else{
            BodyDef bdef = new BodyDef();
            bdef.position.set(getX()+1,getY());
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bdef);

            FixtureDef fdef = new FixtureDef();
            CircleShape shape = new CircleShape();
            shape.setRadius(10 / LevelManager.PPM);
            fdef.filter.categoryBits = LevelManager.CHARACTER_PROYECT;
            fdef.filter.maskBits = LevelManager.ENEMY_BIT
                    | LevelManager.ENEMY_COLLIDER_BIT
                    | LevelManager.OBJECT_BIT
                    | LevelManager.GROUND_BIT
                    | LevelManager.PLATAFORM_BIT
                    | LevelManager.OBSTACULE_BIT;

            fdef.shape = shape;
            b2body.createFixture(fdef).setUserData(this);
        }
    }

    @Override
    public void use() {
        screen.getHud().updateLifes(-1);
        destroy();
        screen.getPlayer().lessLife();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(b2body.getPosition().x -getWidth() / 2.5f , b2body.getPosition().y - getHeight() / 1.3f);
        b2body.setActive(true);
        if((screen.getPlayer().currentState == Character.State.SHOT && b2body.getLinearVelocity().x <= 0.2f)){
            b2body.applyLinearImpulse(new Vector2(0.1f,0),b2body.getWorldCenter(),true);
        }
        else if(b2body.getLinearVelocity().x <= 0.2f){
            b2body.applyLinearImpulse(new Vector2(-0.1f,0),b2body.getWorldCenter(),true);
        }

    }
}

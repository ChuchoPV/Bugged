package com.jpv.Bugged.Niveles.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.jpv.Bugged.Niveles.LevelManager;
import com.jpv.Bugged.Niveles.Screens.PlayScreen;

public class Proyectil extends Item{

    public Proyectil(PlayScreen screen, float x, float y, String enemy) {
        super(screen, x, y);
        b2body.setGravityScale(0);
        if(enemy.equals("spider")){
            setRegion(new TextureRegion(screen.getAtlas().findRegion("spider_prjct"), 0,0,50,70));
        }else if(enemy.equals("slug")){
            setRegion(new TextureRegion(screen.getAtlas().findRegion("slug_prjct"), 0,0,50,70));
        }
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / LevelManager.PPM);
        fdef.filter.categoryBits = LevelManager.ITEM_BIT;
        fdef.filter.maskBits = LevelManager.CHARACTER_BIT
                | LevelManager.OBJECT_BIT
                | LevelManager.GROUND_BIT
                | LevelManager.PLATAFORM_BIT
                | LevelManager.OBSTACULE_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use() {
        screen.getHud().updateLifes(0);
        destroy();
        screen.getPlayer().setLife();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(b2body.getPosition().x -getWidth() / 2.5f , b2body.getPosition().y - getHeight() / 1.3f);
        b2body.setActive(true);
        if(b2body.getLinearVelocity().x <= 0.5f){
            b2body.applyLinearImpulse(new Vector2(-0.1f,0),b2body.getWorldCenter(),true);
        }

    }
}

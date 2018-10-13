package com.jpv.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.jpv.Level1;
import com.jpv.Sprites.Character;
import com.jpv.Sprites.Enemies.Enemy;
import com.jpv.Sprites.Items.Item;
import com.jpv.Sprites.TileObjects.InteractiveTiledObject;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //Oring the category with both collider
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case Level1.CHARACTER_HEAD_BIT | Level1.PLATAFORM_BIT:
            case Level1.CHARACTER_HEAD_BIT | Level1.OBSTACULE_BIT:
                if(fixA.getFilterData().categoryBits == Level1.CHARACTER_HEAD_BIT) {
                    ((InteractiveTiledObject) fixB.getUserData()).onHeadHit();
                }else {
                    ((InteractiveTiledObject) fixA.getUserData()).onHeadHit();
                }
                break;
            case Level1.CHARACTER_ARMA_BIT | Level1.ENEMY_COLLIDER_BIT:
                if(fixA.getFilterData().categoryBits == Level1.ENEMY_COLLIDER_BIT)
                    ((Enemy)fixA.getUserData()).onHeadHit();
                else
                    ((Enemy)fixB.getUserData()).onHeadHit();
                break;
            case Level1.ITEM_BIT | Level1.CHARACTER_BIT:
                if(fixA.getFilterData().categoryBits == Level1.ITEM_BIT)
                    ((Item)fixA.getUserData()).use();
                else
                    ((Item)fixB.getUserData()).use();
                break;
            case Level1.CHARACTER_BIT | Level1.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == Level1.CHARACTER_BIT)
                    ((Character) fixA.getUserData()).hit();
                else
                    ((Character) fixB.getUserData()).hit();
                break;
            case Level1.CHARACTER_BIT | Level1.BOSS_PIES_BIT:
                if(fixA.getFilterData().categoryBits == Level1.CHARACTER_BIT) {
                    ((Character) fixA.getUserData()).kill();
                }
                else {
                    ((Character) fixB.getUserData()).kill();
                }
                break;
            case Level1.CHARACTER_BIT | Level1.BOSS_COLLIDER_BIT:
                if(fixA.getFilterData().categoryBits == Level1.CHARACTER_BIT) {
                    Gdx.app.log("Character","damage");
                }else {
                    Gdx.app.log("Character","damage");
                    break;
                }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

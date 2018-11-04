package com.jpv.Bugged.Niveles.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.jpv.Bugged.Niveles.LevelManager;
import com.jpv.Bugged.Niveles.Sprites.Character;
import com.jpv.Bugged.Niveles.Sprites.Enemies.Enemy;
import com.jpv.Bugged.Niveles.Sprites.Items.Item;
import com.jpv.Bugged.Niveles.Sprites.TileObjects.InteractiveTiledObject;


public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //Oring the category with both collider
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case LevelManager.CHARACTER_HEAD_BIT | LevelManager.PLATAFORM_BIT:
            case LevelManager.CHARACTER_HEAD_BIT | LevelManager.OBSTACULE_BIT:
                if(fixA.getFilterData().categoryBits == LevelManager.CHARACTER_HEAD_BIT) {
                    ((InteractiveTiledObject) fixB.getUserData()).onHeadHit();
                }else {
                    ((InteractiveTiledObject) fixA.getUserData()).onHeadHit();
                }
                break;
            case LevelManager.CHARACTER_ARMA_BIT | LevelManager.ENEMY_COLLIDER_BIT:
                if(fixA.getFilterData().categoryBits == LevelManager.ENEMY_COLLIDER_BIT) {
                    ((Enemy) fixA.getUserData()).onHeadHit();
                }else {
                    ((Enemy) fixB.getUserData()).onHeadHit();
                }
                break;
            case LevelManager.ITEM_BIT | LevelManager.CHARACTER_BIT:
                if(fixA.getFilterData().categoryBits == LevelManager.ITEM_BIT)
                    ((Item)fixA.getUserData()).use();
                else
                    ((Item)fixB.getUserData()).use();
                break;
            case LevelManager.CHARACTER_BIT | LevelManager.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == LevelManager.CHARACTER_BIT)
                    ((Character) fixA.getUserData()).hit();
                else
                    ((Character) fixB.getUserData()).hit();
                break;
            case LevelManager.CHARACTER_BIT | LevelManager.BOSS_PIES_BIT:
                if(fixA.getFilterData().categoryBits == LevelManager.CHARACTER_BIT) {
                    if(((Character) fixA.getUserData()).b2body.getLinearVelocity().y == 0) {
                        ((Character) fixA.getUserData()).kill();
                    }
                }else {
                    if(((Character) fixB.getUserData()).b2body.getLinearVelocity().y == 0) {
                        ((Character) fixB.getUserData()).kill();
                    }
                }
                break;
            case LevelManager.CHARACTER_BIT | LevelManager.BOSS_COLLIDER_BIT:
                if(fixA.getFilterData().categoryBits == LevelManager.CHARACTER_BIT) {
                    ((Character) fixA.getUserData()).hit();
                }
                else {
                    ((Character) fixB.getUserData()).hit();
                }
                break;
            case LevelManager.CHARACTER_ARMA_BIT | LevelManager.BOSS_BIT:
                if(fixA.getFilterData().categoryBits == LevelManager.BOSS_BIT) {
                    ((Enemy) fixA.getUserData()).onHeadHit();
                }
                else {
                    ((Enemy) fixB.getUserData()).onHeadHit();
                }
                break;
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

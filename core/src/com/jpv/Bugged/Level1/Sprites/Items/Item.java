package com.jpv.Bugged.Level1.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jpv.Bugged.Level1.LevelManager;
import com.jpv.Bugged.Level1.Screens.PlayScreen;

public abstract class Item extends Sprite{
    protected PlayScreen screen;
    protected World world;
    protected Boolean toDestroy;
    protected Boolean destroyed;
    protected Body b2body;

    public Item(PlayScreen screen, float x, float y){
        this.screen =screen;
        this.world = screen.getWorld();
        setPosition(x,y);
        setBounds(getX(), getY(), 80 / LevelManager.PPM, 80 / LevelManager.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;

    }

    public abstract void defineItem();
    public abstract void use();

    public void update(float dt){
        if(toDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
        }
    }

    public void draw(Batch batch){
        if(!destroyed){
            super.draw(batch);
        }
    }

    void destroy(){
        toDestroy = true;
    }
}

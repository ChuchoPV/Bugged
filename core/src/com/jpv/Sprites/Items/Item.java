package com.jpv.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jpv.Level1;
import com.jpv.Screens.PlayScreen;
import com.jpv.Sprites.Character;

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
        setBounds(getX(), getY(), 80 / Level1.PPM, 80 / Level1.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;

    }

    public abstract void defineItem();
    public abstract void use(Character player);

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

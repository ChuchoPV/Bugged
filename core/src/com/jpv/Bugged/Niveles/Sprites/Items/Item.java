package com.jpv.Bugged.Niveles.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jpv.Bugged.Niveles.LevelManager;
import com.jpv.Bugged.Niveles.Screens.PlayScreen;

public abstract class Item extends Sprite{
    protected PlayScreen screen;
    protected World world;
    private Boolean toDestroy;
    private Boolean destroyed;
    protected Body b2body;
    private Vector2 velocity;
    protected String enemy;
    private float lifetime;

    public Item(PlayScreen screen, float x, float y, String enemy){
        this.screen =screen;
        this.world = screen.getWorld();
        setPosition(x,y);
        this.enemy = enemy;
        setBounds(getX(), getY(), 80 / LevelManager.PPM, 80 / LevelManager.PPM);
        velocity = new Vector2(0,0);
        defineItem();
        toDestroy = false;
        destroyed = false;
        lifetime = 0;
    }

    public Item(PlayScreen screen, float x, float y){
        this.screen =screen;
        this.world = screen.getWorld();
        this.enemy = "hank";
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
            destroyed = true;
            world.destroyBody(this.b2body);
            if(screen.getItems().size != 0) {
                screen.getItems().pop();
            }
        }
        if(Math.abs(this.b2body.getLinearVelocity().x) <= 0 && lifetime > 0 && !this.enemy.equals("hank")){
            destroyed = true;
            world.destroyBody(this.b2body);
            if(screen.getItems().size != 0) {
                screen.getItems().pop();
            }
        }
        lifetime += dt;
    }

    public void draw(Batch batch){
        if(!destroyed){
            super.draw(batch);
        }
    }

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;

        if(y)
            velocity.y = -velocity.y;
    }

    public void destroy(){
        toDestroy = true;
    }
}

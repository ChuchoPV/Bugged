package com.jpv.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jpv.Screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected TiledMap map;
    public Body b2body;
    public Vector2 velocity;
    public PlayScreen screen;
    protected MapObject object;

    public Enemy(PlayScreen screen, float x, float y, MapObject object){
        this.object = object;
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(0,0.5f);
        b2body.setActive(false);

    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void onHeadHit();

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;

        if(y)
            velocity.y = -velocity.y;
    }
}

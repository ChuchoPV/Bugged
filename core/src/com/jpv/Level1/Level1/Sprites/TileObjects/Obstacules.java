package com.jpv.Level1.Level1.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.jpv.Level1.Level1.Level1;
import com.jpv.Level1.Level1.Screens.PlayScreen;

public class Obstacules extends InteractiveTiledObject {
    public Obstacules(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategotyFilter(Level1.OBSTACULE_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Obstacules", "Collision");
    }

    @Override
    public void update() {
        if(setToDesrtoy){
            world.destroyBody(body);
        }
    }

    @Override
    public void destroy() {
        setToDesrtoy = true;
    }
}

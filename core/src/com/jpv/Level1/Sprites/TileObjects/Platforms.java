package com.jpv.Level1.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.jpv.Level1.Level1;
import com.jpv.Level1.Screens.PlayScreen;

public class Platforms extends InteractiveTiledObject {
    public Platforms(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategotyFilter(Level1.PLATAFORM_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Platforms", "Collision");
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


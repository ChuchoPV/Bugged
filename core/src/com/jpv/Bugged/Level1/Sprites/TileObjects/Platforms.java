package com.jpv.Bugged.Level1.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.jpv.Bugged.Level1.LevelManager;
import com.jpv.Bugged.Level1.Screens.PlayScreen;

public class Platforms extends InteractiveTiledObject {
    public Platforms(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(LevelManager.PLATAFORM_BIT);
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


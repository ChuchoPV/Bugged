package com.jpv.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.jpv.Level1;
import com.jpv.Screens.PlayScreen;

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
}

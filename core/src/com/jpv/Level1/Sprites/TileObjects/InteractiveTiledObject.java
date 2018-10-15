package com.jpv.Level1.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.jpv.Level1.Level1;
import com.jpv.Level1.Screens.PlayScreen;

public abstract class InteractiveTiledObject {
    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    public Body body;
    protected PlayScreen screen;
    public MapObject object;

    protected Fixture fixture;
    protected boolean setToDesrtoy;

    public InteractiveTiledObject(PlayScreen screen, MapObject object){
        this.object = object;
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();
        setToDesrtoy = false;

        BodyDef bdef = new BodyDef();  //Before creating a body we need to say it in what it consist of
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();


        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / Level1.PPM, (bounds.getY() + bounds.getHeight() / 2) /Level1.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / Level1.PPM, bounds.getHeight() / 2 / Level1.PPM );
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    public abstract void onHeadHit();
    public abstract void update();
    public abstract void destroy();

    public void setCategotyFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

}

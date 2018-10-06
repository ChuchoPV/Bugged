package com.jpv.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.jpv.Level1;
import com.jpv.Screens.PlayScreen;
import com.jpv.Sprites.Enemies.Mosquito;
import com.jpv.Sprites.TileObjects.Obstacules;
import com.jpv.Sprites.TileObjects.Platforms;


public class B2WorldCreator {
    private Array<Mosquito> mosquitos;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();  //Before creating a body we need to say it in what it consist of
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //To introduce every object and say to it what it is
        // Create ground bodies / textures
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Level1.PPM, (rect.getY() + rect.getHeight() / 2) /Level1.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / Level1.PPM, rect.getHeight() / 2 / Level1.PPM );
            fdef.shape = shape;
            fdef.filter.categoryBits = Level1.GROUND_BIT;
            body.createFixture(fdef);

        }

        // Create obstacules bodies / textures
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {

            new Obstacules(screen,object);

        }

        // Create platforms bodies / textures
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {

            new Platforms(screen,object);

        }

        //Create mosquitos
        mosquitos = new Array<Mosquito>();
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            mosquitos.add(new Mosquito(screen, rect.getX() / Level1.PPM, rect.getY() / Level1.PPM, object));

        }

        for (MapObject object : map.getLayers().get(6).getObjects().getByType(PolygonMapObject.class)) {
            Polygon rect = ((PolygonMapObject) object).getPolygon();
            mosquitos.add(new Mosquito(screen, rect.getX() / Level1.PPM, rect.getY() / Level1.PPM, object));

        }

    }

    public Array<Mosquito> getMosquitos() {
        return mosquitos;
    }


}

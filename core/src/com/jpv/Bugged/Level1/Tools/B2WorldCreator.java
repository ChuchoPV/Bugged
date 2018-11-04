package com.jpv.Bugged.Level1.Tools;

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
import com.jpv.Bugged.Level1.LevelManager;
import com.jpv.Bugged.Level1.Screens.PlayScreen;
import com.jpv.Bugged.Level1.Sprites.Enemies.Mosquito;
import com.jpv.Bugged.Level1.Sprites.Enemies.TheRedBug;
import com.jpv.Bugged.Level1.Sprites.TileObjects.Obstacules;
import com.jpv.Bugged.Level1.Sprites.TileObjects.Platforms;


public class B2WorldCreator {
    private Array<Mosquito> mosquitos;
    private TheRedBug theRedBug;
    private Array<Obstacules> obstacules;
    private Array<Platforms> platforms;

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
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / LevelManager.PPM, (rect.getY() + rect.getHeight() / 2) / LevelManager.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / LevelManager.PPM, rect.getHeight() / 2 / LevelManager.PPM );
            fdef.shape = shape;
            fdef.filter.categoryBits = LevelManager.GROUND_BIT;
            body.createFixture(fdef);

        }

        obstacules = new Array<Obstacules>();
        // Create obstacules bodies / textures
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            if(!object.getProperties().containsKey("Boss"))
                obstacules.add(new Obstacules(screen,object));
        }

        platforms = new Array<Platforms>();
        // Create platforms bodies / textures
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            platforms.add(new Platforms(screen,object));
        }

        //Create mosquitos
        mosquitos = new Array<Mosquito>();
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            mosquitos.add(new Mosquito(screen, rect.getX() / LevelManager.PPM, rect.getY() / LevelManager.PPM, object));

        }

        for (MapObject object : map.getLayers().get(5).getObjects().getByType(PolygonMapObject.class)) {
            Polygon rect = ((PolygonMapObject) object).getPolygon();
            mosquitos.add(new Mosquito(screen, rect.getX() / LevelManager.PPM, rect.getY() / LevelManager.PPM, object));

        }

        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            theRedBug = new TheRedBug(screen,rect.getX() / LevelManager.PPM, rect.getY() / LevelManager.PPM,object);
        }

    }

    public Array<Mosquito> getMosquitos() { return mosquitos; }
    public TheRedBug getTheRedBug() { return theRedBug; }
    public Array<Obstacules> getObstacules() {
        return obstacules;
    }
    public Array<Platforms> getPlatforms() {
        return platforms;
    }
}

package mx.itesm.Bugged.Niveles.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import mx.itesm.Bugged.Niveles.LevelManager;
import mx.itesm.Bugged.Niveles.Sprites.Enemies.Slug;
import mx.itesm.Bugged.Niveles.Sprites.Enemies.TheKing;
import mx.itesm.Bugged.Niveles.Sprites.Enemies.TheRedBug;
import mx.itesm.Bugged.Niveles.Screens.PlayScreen;
import mx.itesm.Bugged.Niveles.Sprites.Enemies.Mosquito;
import mx.itesm.Bugged.Niveles.Sprites.Enemies.Spider;
import mx.itesm.Bugged.Niveles.Sprites.TileObjects.Obstacules;
import mx.itesm.Bugged.Niveles.Sprites.TileObjects.Platforms;


public class B2WorldCreator {
    private Array<Mosquito> mosquitos;
    private Array<Slug> slugs;
    private Array<Spider> spiders;
    private TheRedBug theRedBug;
    private TheKing theking;
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
            if(!object.getProperties().containsKey("Boss")) {
                obstacules.add(new Obstacules(screen, object));
            }
        }

        platforms = new Array<Platforms>();
        // Create platforms bodies / textures
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
                platforms.add(new Platforms(screen,object));
        }

        //Create Enemies
        mosquitos = new Array<Mosquito>();
        slugs = new Array<Slug>();
        spiders = new Array<Spider>();
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if(object.getProperties().containsKey("Slug")) {
                slugs.add(new Slug(screen, rect.getX() / LevelManager.PPM, rect.getY() / LevelManager.PPM, object));
            }else if(object.getProperties().containsKey("Spider")) {
                spiders.add(new Spider(screen, rect.getX() / LevelManager.PPM, rect.getY() / LevelManager.PPM, object));
            }else{
                mosquitos.add(new Mosquito(screen, rect.getX() / LevelManager.PPM, rect.getY() / LevelManager.PPM, object));
            }

        }

        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if(object.getProperties().containsKey("King")) {
                theking = new TheKing(screen, rect.getX() / LevelManager.PPM, rect.getY() / LevelManager.PPM, object);
            }else{
                theRedBug = new TheRedBug(screen, rect.getX() / LevelManager.PPM, rect.getY() / LevelManager.PPM, object);
            }
        }

    }

    public Array<Mosquito> getMosquitos() { return mosquitos; }
    public Array<Slug> getSlugs() {
        return slugs;
    }
    public Array<Spider> getSpiders() {
        return spiders;
    }
    public TheRedBug getTheRedBug() { return theRedBug; }
    public TheKing getTheking() {
        return theking;
    }
}

package mx.itesm.Bugged.Niveles.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import mx.itesm.Bugged.Niveles.LevelManager;
import mx.itesm.Bugged.Niveles.Screens.PlayScreen;

public abstract class Item extends Sprite{
    protected PlayScreen screen;
    protected World world;
    private Boolean toDestroy;
    private Boolean destroyed;
    protected Body b2body;
    protected String enemy;

    public Item(PlayScreen screen, float x, float y, String enemy){
        this.screen =screen;
        this.world = screen.getWorld();
        setPosition(x,y);
        this.enemy = enemy;
        setBounds(getX(), getY(), 80 / LevelManager.PPM, 80 / LevelManager.PPM);
        defineItem();
        this.toDestroy = false;
        this.destroyed = false;
    }

    public abstract void defineItem();
    public abstract void use();

    public void update(float dt){
        if(this.toDestroy && !this.destroyed){
            this.destroyed = true;
            world.destroyBody(this.b2body);
            if (screen.getHearts().size != 0) {
                screen.getHearts().peek();
            }
            if (screen.getProyectiles().size != 0) {
                screen.getProyectiles().peek();
            }
        }
    }

    public void draw(Batch batch){
        if(!destroyed){
            super.draw(batch);
        }
    }


    public void destroy(){
        this.toDestroy = true;
    }
}

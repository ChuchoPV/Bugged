package mx.itesm.Bugged.Niveles.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import mx.itesm.Bugged.Niveles.LevelManager;
import mx.itesm.Bugged.Niveles.Screens.PlayScreen;

public class ProyectilHank extends Sprite {
    private boolean fliped;
    protected PlayScreen screen;
    protected World world;
    private Boolean toDestroy;
    private Boolean destroyed;
    protected Body b2body;
    private float velocity;
    private float lifetime;
    private boolean active;

    public ProyectilHank(PlayScreen screen, float x, float y, boolean fliped) {
        this.fliped = fliped;
        this.screen =screen;
        this.world = screen.getWorld();
        TextureRegion region = new TextureRegion(screen.getAtlas().findRegion("projectile_salt"), 0, 0, 50, 70); //Hank_Shoot
        region.flip(this.fliped, false);
        setRegion(region);
        setPosition(x,y);
        setBounds(getX(), getY(), 80 / LevelManager.PPM, 80 / LevelManager.PPM);
        defineItem();
        this.b2body.setGravityScale(0);
        this.b2body.setActive(true);
        this.toDestroy = false;
        this.destroyed = false;
        this.lifetime = 0;
        this.velocity = 0;
        this.active = true;
    }

    private void defineItem() {
        BodyDef bdef = new BodyDef();
        if(!this.fliped){
            bdef.position.set(getX() + 1, getY());
        }else{
            bdef.position.set(getX() - 1, getY());
        }

        bdef.type = BodyDef.BodyType.DynamicBody;
        this.b2body = world.createBody(bdef);


        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / LevelManager.PPM);
        fdef.filter.categoryBits = LevelManager.CHARACTER_PROYECT;
        fdef.filter.maskBits = LevelManager.ENEMY_BIT
                | LevelManager.SHOTTER_CONTACT
                | LevelManager.OBJECT_BIT
                | LevelManager.GROUND_BIT
                | LevelManager.PLATAFORM_BIT
                | LevelManager.OBSTACULE_BIT;

        fdef.shape = shape;
        this.b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if(!destroyed){
            super.draw(batch);
        }
    }

    public void update(float dt) {
        if(this.toDestroy && !this.destroyed){
            this.destroyed = true;
            world.destroyBody(this.b2body);
            if(screen.getProyectilesHank().size != 0){
                screen.getProyectilesHank().pop();
            }
        }
        if(Math.abs(this.b2body.getLinearVelocity().x) <= 0 && lifetime > 0){
            destroy();
        }
        lifetime += dt;

        setPosition(b2body.getPosition().x - getWidth() / 2.5f, b2body.getPosition().y - getHeight() / 1.3f);
        velocity = 1;
        if (active) {
            if (!this.fliped) {
                this.b2body.getPosition().set(new Vector2(getX()-5,getY()));
                if (this.b2body.getLinearVelocity().x < velocity * 10) {
                    this.b2body.applyLinearImpulse(new Vector2(velocity, 0), this.b2body.getWorldCenter(), true);
                } else {
                    this.b2body.setLinearVelocity(velocity * 10, 0);
                    this.active=false;
                }
            } else {
                if (this.b2body.getLinearVelocity().x > -velocity * 10) {
                    this.b2body.applyLinearImpulse(new Vector2(-velocity, 0), this.b2body.getWorldCenter(), true);
                } else {
                    this.b2body.setLinearVelocity(-velocity * 10, 0);
                    this.active=false;
                }
            }
        }
    }

    public void destroy(){
        this.toDestroy = true;
    }
}

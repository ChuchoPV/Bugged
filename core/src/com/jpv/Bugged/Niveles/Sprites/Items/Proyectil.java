package com.jpv.Bugged.Niveles.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.jpv.Bugged.Niveles.LevelManager;
import com.jpv.Bugged.Niveles.Screens.PlayScreen;

public class Proyectil extends Item{
    private boolean first;
    private boolean fliped;
    private float velocity=1;
    private boolean active=true;

    public Proyectil(PlayScreen screen, float x, float y, String enemy, boolean fliped) {
        super(screen, x, y, enemy);
        first = true;
        this.fliped = fliped;
        b2body.setGravityScale(0);
        if(enemy.equals("spider")) {
            setRegion(new TextureRegion(screen.getAtlas().findRegion("spider_prjct"), 0, 0, 50, 70));
        }else if(enemy.equals("slug")) {
            //setRegion(new TextureRegion(screen.getAtlas().findRegion("slug_prjct"), 0, 0, 50, 70));
            TextureRegion region = new TextureRegion(screen.getAtlas().findRegion("slug_prjct"), 0, 0, 50, 70); //Hank_Shoot
            region.flip(this.fliped,false);
            setRegion(region);
        }
    }
    
    @Override
    public void defineItem() {
            BodyDef bdef = new BodyDef();
            bdef.position.set(getX(),getY());
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bdef);

            FixtureDef fdef = new FixtureDef();
            CircleShape shape = new CircleShape();
            shape.setRadius(10 / LevelManager.PPM);
            fdef.filter.categoryBits = LevelManager.ENEMY_PROYECT;
            fdef.filter.maskBits = LevelManager.CHARACTER_BIT
                    | LevelManager.OBJECT_BIT
                    | LevelManager.GROUND_BIT
                    | LevelManager.PLATAFORM_BIT
                    | LevelManager.OBSTACULE_BIT;

            fdef.shape = shape;
            b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use() {
        screen.getHud().updateLifes(-1);
        screen.getPlayer().lessLife();
        super.destroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(this.b2body.getPosition().x - getWidth() / 2.5f, this.b2body.getPosition().y - getHeight() / 1.3f);
        this.b2body.setActive(true);
        //System.out.println(this.fliped);


        if (active) {
            if (this.fliped) {
                if (this.b2body.getLinearVelocity().x < velocity * 10) {
                    this.b2body.applyLinearImpulse(new Vector2(velocity, 0), this.b2body.getWorldCenter(), true);
                } else {
                    this.b2body.setLinearVelocity(velocity * 10, 0);
                    active=false;
                }
            } else {
                if (this.b2body.getLinearVelocity().x > -velocity * 10) {
                    this.b2body.applyLinearImpulse(new Vector2(-velocity, 0), this.b2body.getWorldCenter(), true);
                } else {
                    this.b2body.setLinearVelocity(-velocity * 10, 0);
                    active=false;
                }
            }
        }
    }
}

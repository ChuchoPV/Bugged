package com.jpv.Bugged.Niveles.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.jpv.Bugged.Niveles.LevelManager;
import com.jpv.Bugged.Niveles.Screens.PlayScreen;

public class ProyectilHank extends Item {
    private boolean first;
    private boolean fliped;
    private float velocity = 1;

    public ProyectilHank(PlayScreen screen, float x, float y, boolean fliped) {
        super(screen, x, y);
        this.first = true;
        this.fliped = fliped;
        this.b2body.setGravityScale(0);
        TextureRegion region = new TextureRegion(screen.getAtlas().findRegion("projectile_salt"), 0, 0, 50, 70); //Hank_Shoot
        region.flip(this.fliped, false);
        setRegion(region);

    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() + 1, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

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
        b2body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void use() {
        screen.getHud().updateLifes(-1);
        destroy();
        screen.getPlayer().lessLife();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(b2body.getPosition().x - getWidth() / 2.5f, b2body.getPosition().y - getHeight() / 1.3f);
        b2body.setActive(true);
        /*
        if ((screen.getPlayer().currentState == Character.State.SHOT)) {
            //this.b2body.applyLinearImpulse(new Vector2(0.1f,0),b2body.getWorldCenter(),true);
            this.b2body.setLinearVelocity(.2f, 0);
        }*/

        if (!this.fliped) {
            if (this.b2body.getLinearVelocity().x < velocity * 10) {
                this.b2body.applyLinearImpulse(new Vector2(velocity, 0), this.b2body.getWorldCenter(), true);
            } else {
                this.b2body.setLinearVelocity(velocity * 10, 0);
            }


        } else {
            if (this.b2body.getLinearVelocity().x > -velocity * 10) {
                this.b2body.applyLinearImpulse(new Vector2(-velocity, 0), this.b2body.getWorldCenter(), true);
            } else {
                this.b2body.setLinearVelocity(-velocity * 10, 0);
            }

        /*
        else if(b2body.getLinearVelocity().x <= 0.2f){
            this.b2body.applyLinearImpulse(new Vector2(-0.1f,0),b2body.getWorldCenter(),true);
        }
        if (this.b2body.getLinearVelocity().x<=-4f)
            this.b2body.setLinearVelocity(-4f,0);

        if(first) {
            super.reverseVelocity(fliped, false);
            first = false;
        }*/
        }

    }
}

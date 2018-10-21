package com.jpv.Level1.Level1.Sprites.Items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.jpv.Level1.Level1.Level1;
import com.jpv.Level1.Level1.Screens.PlayScreen;


public class Heart extends Item {
    public Heart(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(new TextureRegion(new Texture("Heart.png")));
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(40 / Level1.PPM);
        fdef.filter.categoryBits = Level1.ITEM_BIT;
        fdef.filter.maskBits = Level1.CHARACTER_BIT
                | Level1.OBJECT_BIT
                | Level1.GROUND_BIT
                | Level1.PLATAFORM_BIT
                | Level1.OBSTACULE_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use() {
        screen.getHud().updateLifes(0);
        destroy();
        screen.getPlayer().setLife(1);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(b2body.getPosition().x -getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        b2body.setActive(true);

    }
}

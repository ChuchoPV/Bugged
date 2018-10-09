package com.jpv.Sprites.Items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.jpv.Level1;
import com.jpv.Screens.PlayScreen;
import com.jpv.Sprites.Character;

import javax.sound.sampled.LineEvent;

public class Heart extends Item {
    public Heart(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(new TextureRegion(new Texture("Heart1.png")));
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
    public void use(Character player) {
        destroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(b2body.getPosition().x -getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

    }
}

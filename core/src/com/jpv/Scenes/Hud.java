package com.jpv.Scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jpv.Level1;

public class Hud {
    public Sprite heart1;
    public Sprite heart2;
    public Sprite heart3;
    private SpriteBatch sb;

    public Hud(SpriteBatch sb, float ANCHO, float ALTO,float x, float y){
        this.sb = sb;
        heart1 = new Sprite(new Texture("Heart1.png"));
        heart1.setPosition(x / Level1.PPM, y / Level1.PPM);
        heart2 = new Sprite(new Texture("Heart1.png"));
        heart2.setPosition((10 + 5 + heart2.getWidth()) / Level1.PPM, (ALTO - heart2.getHeight() - 10) / Level1.PPM);
        heart3 = new Sprite(new Texture("Heart1.png"));
        heart3.setPosition((10 + 10 + (heart3.getWidth() * 2)) / Level1.PPM, (ALTO - heart3.getHeight() - 10) / Level1.PPM);
    }

    public void handleInput(float dt){
        heart1.setX((heart1.getX() + (300 / Level1.PPM *dt)) / Level1.PPM);
        heart2.setX((heart1.getX() + (300 / Level1.PPM *dt)) / Level1.PPM);
        heart3.setX((heart1.getX() + (300 / Level1.PPM *dt)) / Level1.PPM);
    }

    public void render(float dt){
        handleInput(dt);
        heart1.draw(sb);
        heart2.draw(sb);
        heart3.draw(sb);
    }

    public void dispose(){

    }

}

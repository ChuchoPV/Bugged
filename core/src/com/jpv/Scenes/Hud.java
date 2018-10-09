package com.jpv.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jpv.Level1;

public class Hud {
    public Stage stage;
    private Viewport viewport;

    public Sprite heart;

    public Hud(SpriteBatch sb){
        heart = new Sprite(new Texture("Heart.png"));
        heart.setPosition(Level1.V_WIDTH / Level1.PPM, Level1.V_HEIGHT / Level1.PPM);

    }

    public void update(){

    }

    public void dispose(){
        stage.dispose();
    }

}

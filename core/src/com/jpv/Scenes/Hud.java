package com.jpv.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jpv.Level1;

public class Hud {
    public Stage stage;
    private Viewport viewport;

    public Texture heart;

    public Hud(SpriteBatch sb){
        viewport = new FitViewport(Level1.V_WIDTH,Level1.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,sb);
        Array<Image> vidas = new Array<Image>();
        heart = new Texture("Heart.png");
        TextureRegionDrawable heartReg = new TextureRegionDrawable(new TextureRegion(heart));
        Image img;

        int y = 1;
        for (int i= 0; i < 3; i++){
            img = new Image(heartReg);
            img.setPosition( (Level1.V_WIDTH / Level1.PPM) + (y / Level1.PPM), Level1.V_WIDTH / Level1.PPM);
            vidas.add(img);
            y += 5000;
        }

        for(Image imga : vidas)
            stage.addActor(imga);

    }

    public void update(){

    }

    public void dispose(){
        stage.dispose();
    }

}

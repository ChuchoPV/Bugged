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
    public static Stage stage;
    private static Array<Image> vidas;
    private static int y;

    private static Sprite heart;

    public Hud(SpriteBatch sb){
        Viewport viewport = new FitViewport(Level1.V_WIDTH, Level1.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport,sb);
        vidas = new Array<Image>();

        Sprite image = new Sprite(new Texture("Hank_Image.png"));
        heart = new Sprite(new Texture("Heart.png"));
        TextureRegionDrawable heartReg = new TextureRegionDrawable(new TextureRegion(heart));
        TextureRegionDrawable imgReg = new TextureRegionDrawable(new TextureRegion(image));
        Image img;
        Image foto = new Image(imgReg);
        foto.setPosition((Level1.V_WIDTH / Level1.PPM), (Level1.V_WIDTH / Level1.PPM ) + 600);

        y = 10000;
        for (int i= 0; i < 3; i++){
            img = new Image(heartReg);
            img.setPosition( (Level1.V_WIDTH / Level1.PPM) + (y / Level1.PPM), (Level1.V_WIDTH / Level1.PPM ) + 600);
            vidas.add(img);
            y += 8000;
        }
        stage.addActor(foto);
        for(Image imga : vidas)
            stage.addActor(imga);

    }

    public static void updateLifes(boolean less){
        if(less){
            stage.getActors().pop();
            y -= 8000;

        }else{
            Image img = new Image(heart);
            img.setPosition((Level1.V_WIDTH / Level1.PPM) + (y / Level1.PPM), (Level1.V_WIDTH / Level1.PPM ) + 600);
            stage.getActors().add(img);
        }
    }


    public void dispose(){
        stage.dispose();
    }

}

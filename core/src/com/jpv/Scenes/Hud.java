package com.jpv.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jpv.Level1;
import com.jpv.Screens.PlayScreen;
import com.jpv.Sprites.Character;
import com.jpv.Tools.GenericButton;

public class Hud {
    public static Stage stage;
    private static int y;
    private PlayScreen screen;

    private static Sprite heart;

    public Hud(PlayScreen screen){
        this.screen = screen;
        Viewport viewport = new FitViewport(Level1.V_WIDTH, Level1.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport,screen.getGame().batch);
        Gdx.input.setInputProcessor(stage);
        Array<Image> vidas = new Array<Image>();

        Sprite image = new Sprite(new Texture("Hank_Image.png"));
        heart = new Sprite(new Texture("Heart.png"));
        TextureRegionDrawable heartReg = new TextureRegionDrawable(new TextureRegion(heart));
        TextureRegionDrawable imgReg = new TextureRegionDrawable(new TextureRegion(image));
        Image img;
        Image foto = new Image(imgReg);
        foto.setPosition((Level1.V_WIDTH / Level1.PPM), (Level1.V_WIDTH / Level1.PPM ) + 600);
        
        createButtons();

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

    private void createButtons() {

        GenericButton btnJoystickUp = new GenericButton((Level1.V_WIDTH / Level1.PPM) + 50 , (Level1.V_WIDTH / Level1.PPM ) + 50,"Joystick_up.png","vacia.png");
        btnJoystickUp.button().addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent event, float x, float y) {
                 super.clicked(event, x, y);
                 if(screen.getPlayer().b2body.getPosition().y < (Level1.V_HEIGHT + 500) / Level1.PPM
                    && (screen.getPlayer().currentState== Character.State.RUNNING || screen.getPlayer().currentState==Character.State.STANDING)) {
                     screen.getPlayer().b2body.applyLinearImpulse(new Vector2(0, 8f), screen.getPlayer().b2body.getWorldCenter(), true);
                 }
                }
         }
        );

        GenericButton btnJoystickRight = new GenericButton((Level1.V_WIDTH / Level1.PPM) + (10000 / Level1.PPM), (Level1.V_WIDTH / Level1.PPM ),"Joystick_Right.png","vacia.png");
        btnJoystickRight.button().addListener(new ClickListener() {
               @Override
               public void clicked(InputEvent event, float x, float y) {
                   super.clicked(event, x, y);
                   if(screen.getPlayer().b2body.getLinearVelocity().x <= 2 && screen.getPlayer().currentState != Character.State.DAMAGED) {
                       screen.getPlayer().b2body.applyLinearImpulse(new Vector2(0.5f, 0), screen.getPlayer().b2body.getWorldCenter(), true);
                   }
               }
           }
        );

        GenericButton btnJoystickLeft = new GenericButton((Level1.V_WIDTH / Level1.PPM), (Level1.V_WIDTH / Level1.PPM ),"Joystick_left.png","vacia.png");
        btnJoystickLeft.setPlace((Level1.V_WIDTH / Level1.PPM), (Level1.V_WIDTH / Level1.PPM ));
        btnJoystickLeft.button().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.getPlayer().b2body.applyLinearImpulse(new Vector2(-0.5f, 0), screen.getPlayer().b2body.getWorldCenter(), true);
                if(screen.getPlayer().b2body.getLinearVelocity().x >= -2 && screen.getPlayer().currentState != Character.State.DAMAGED){
                    screen.getPlayer().b2body.applyLinearImpulse(new Vector2(-0.5f, 0), screen.getPlayer().b2body.getWorldCenter(), true);
                }
              }
        }
        );

        stage.addActor(btnJoystickUp.button());
        stage.addActor(btnJoystickRight.button());
        stage.addActor(btnJoystickLeft.button());
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

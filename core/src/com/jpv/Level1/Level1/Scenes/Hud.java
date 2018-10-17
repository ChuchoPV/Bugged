package com.jpv.Level1.Level1.Scenes;

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
import com.jpv.Level1.Level1.Level1;
import com.jpv.Level1.Level1.Screens.PlayScreen;
import com.jpv.Level1.Level1.Sprites.Character;
import com.jpv.Level1.Level1.Tools.GenericButton;

public class Hud {
    public static Stage stage;
    private static int y;
    private PlayScreen screen;
    private boolean btnLef;
    private boolean btnRig;
    public static boolean btnAt;
    private boolean first;
    private static Array<Image> vidas;

    private static Sprite heart;

    public Hud(PlayScreen screen){
        this.screen = screen;
        Viewport viewport = new FitViewport(Level1.V_WIDTH, Level1.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport,screen.getGame().batch);
        first = true;
        Gdx.input.setInputProcessor(stage);
        vidas = new Array<Image>();

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

        //region RIGHT BUTTON
        GenericButton btnJoystickRight = new GenericButton((Level1.V_WIDTH / Level1.PPM) + (15000 / Level1.PPM), (Level1.V_WIDTH / Level1.PPM ),"Joystick_Right.png","vacia.png");
        btnJoystickRight.button().addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(first){
                    btnRig = true;
                    first = false;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                btnRig = false;
                first = true;
            }
        }
        );
        //endregion
        //region LEFT BUTTON
        GenericButton btnJoystickLeft = new GenericButton((Level1.V_WIDTH / Level1.PPM) + 20, (Level1.V_WIDTH / Level1.PPM ),"Joystick_left.png","vacia.png");
        btnJoystickLeft.button().addListener(new ClickListener() {
             @Override
             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                 if(first){
                     btnLef = true;
                     first = false;
                 }
                 //screen.getPlayer().b2body.applyLinearImpulse(15f,0f,0,0,true);
                 return super.touchDown(event, x, y, pointer, button);
             }

             @Override
             public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                 super.touchUp(event, x, y, pointer, button);
                 btnLef = false;
                 first = true;
             }
        }
        );
        //endregion
        //region JUMP BUTTON
        GenericButton btnJoystickUp = new GenericButton((Level1.V_WIDTH / Level1.PPM) + 1100, (Level1.V_WIDTH / Level1.PPM ) + 100,"Attack_Btn.png","vacia.png");
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
        //endregion
        //region ATTACK BUTTON
        GenericButton btnAttack = new GenericButton((Level1.V_WIDTH / Level1.PPM) + 1000, (Level1.V_WIDTH / Level1.PPM ),"Secondary_Btn.png","vacia.png");
        btnAttack.button().addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               super.clicked(event, x, y);
               btnAt = true;
           }
        }
        );
        //endregion

        stage.addActor(btnJoystickUp.button());
        stage.addActor(btnJoystickRight.button());
        stage.addActor(btnJoystickLeft.button());
        stage.addActor(btnAttack.button());
    }

    public static void updateLifes(int less){
        if(less == -1){
            stage.getActors().pop();
            if(vidas.size != 0) {
                vidas.pop();
            }
            y -= 8000;

        }else if(less == 0){
            Image img = new Image(heart);
            img.setPosition((Level1.V_WIDTH / Level1.PPM) + (y / Level1.PPM), (Level1.V_WIDTH / Level1.PPM ) + 600);
            vidas.add(img);
            stage.getActors().add(img);
        }else{
            for(int i = 0; i < vidas.size; i++){
                stage.getActors().pop();
            }
        }
    }

    public boolean getBtnRig(){
        return btnRig;
    }
    public boolean getBtnLef(){
        return btnLef;
    }
    public static boolean getBtnAt(){
        return btnAt;
    }

    public void dispose(){
        stage.dispose();
    }

}

package com.jpv.Bugged.PantallasMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jpv.Bugged.Niveles.Tools.GenericButton;
import com.jpv.Bugged.PantallasMenu.Tools.Pantalla;

public class PantallaOption extends Pantalla {
    private final Bugged pantallaInicio;
    Texture fondoOptions;
    public Stage escenaOptions;

    public PantallaOption(Bugged pantallaInicio) {
        this.pantallaInicio = pantallaInicio;
    }


    @Override
    public void show() {
        crearEscena();
        fondoOptions = new Texture("Options/Options.png");
        Gdx.input.setInputProcessor(escenaOptions);
    }

    private void crearEscena() {
        escenaOptions = new Stage(vista);

        //Boton back
        GenericButton btnBack = new GenericButton(ANCHO, ALTO,"PrincipalScreen/back_btn.png","PrincipalScreen/back_btn_pressed.png");
        btnBack.setPlace(ANCHO-btnBack.button().getWidth(),ALTO-btnBack.button().getHeight()-25);
        btnBack.button().addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent event, float x, float y) {
                 super.clicked(event, x, y);
                 pantallaInicio.setScreen(new PantallaMenuPrincipal(pantallaInicio));
             }
         }
        );

        escenaOptions.addActor(btnBack.button());

    }

    @Override
    public void render(float delta) {
        borrarPantalla(1,1,0.5f);
        batch.setProjectionMatrix((camara.combined));
        batch.begin();
        batch.draw(fondoOptions,0,0);
        batch.end();
        escenaOptions.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}

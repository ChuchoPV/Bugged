package com.jpv.Level1.PantallasMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jpv.Level1.Level1.Tools.GenericButton;
import com.jpv.Level1.PantallasMenu.Tools.Pantalla;

public class PantallaMenuPrincipal extends Pantalla {
    private final Bugged pantallaInicio;
    public Texture fondoMenuPrincipal;
    public Stage escenaMenuPrincipal;
    public Sprite titulo;
    public float angulo;

    public PantallaMenuPrincipal(Bugged pantallaInicio) {
        this.pantallaInicio = pantallaInicio;
    }

    @Override
    public void show() {
        crearEscena();
        fondoMenuPrincipal = new Texture("PrincipalScreen/Inicio.png");
        titulo = new Sprite(new Texture("PrincipalScreen/Titulo.png"));
        titulo.setPosition(ANCHO / 6-80, ALTO / 2+120);
        Gdx.input.setInputProcessor(escenaMenuPrincipal);
    }

    private void crearEscena() {
        escenaMenuPrincipal = new Stage(vista);

        //Boton Play
        GenericButton btnPlay =  new GenericButton(ANCHO / 6-25, ALTO / 2,"PrincipalScreen/Play_Btn.png","PrincipalScreen/Play_Btn_Pressed.png");
        btnPlay.button().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //pantallaInicio.setScreen(new PantallaJuego(pantallaInicio));
                pantallaInicio.setScreen(new PantallaLevelSelect(pantallaInicio));
            }
        }
        );

        //Boton Options
        GenericButton btnOp =  new GenericButton(ANCHO / 6-25, ALTO / 2-btnPlay.button().getHeight()-10,"PrincipalScreen/Options_btn.png","PrincipalScreen/Options_btn_pressed.png");
        btnOp.button().addListener(new ClickListener() {
              @Override
              public void clicked(InputEvent event, float x, float y) {
                  super.clicked(event, x, y);
                  //pantallaInicio.setScreen(new PantallaJuego(pantallaInicio));
                  //pantallaInicio.setScreen(new PantallaOption(pantallaInicio));
              }
          }
        );

        //Boton Help
        GenericButton btnHelp =  new GenericButton(ANCHO / 6+20, ALTO / 2-btnOp.button().getHeight()-125,"PrincipalScreen/Help_btn.png","PrincipalScreen/Help_btn_pressed.png");
        btnHelp.button().addListener(new ClickListener() {
              @Override
              public void clicked(InputEvent event, float x, float y) {
                  super.clicked(event, x, y);
                  //pantallaInicio.setScreen(new PantallaJuego(pantallaInicio));
                  //pantallaInicio.setScreen(new PantallaHelp(pantallaInicio));
              }
          }
        );

        //Boton About
        GenericButton btnAbout =  new GenericButton(ANCHO / 6+200, ALTO / 2-btnOp.button().getHeight()-125,"PrincipalScreen/About_btn.png","PrincipalScreen/About_btn_pressed.png");
        btnAbout.button().addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent event, float x, float y) {
                 super.clicked(event, x, y);
                 //pantallaInicio.setScreen(new PantallaJuego(pantallaInicio));
                 pantallaInicio.setScreen(new PantallaAboutUs(pantallaInicio));
             }
         }
        );

        escenaMenuPrincipal.addActor(btnPlay.button());
        escenaMenuPrincipal.addActor(btnOp.button());
        escenaMenuPrincipal.addActor(btnHelp.button());
        escenaMenuPrincipal.addActor(btnAbout.button());
    }

        @Override
    public void render(float delta) {
            borrarPantalla(1,1,0.5f);
            batch.setProjectionMatrix((camara.combined));
            batch.begin();
            batch.draw(fondoMenuPrincipal,0,0);
            titulo.draw(batch);
            batch.end();
            escenaMenuPrincipal.draw();

            double y = Math.sin(angulo)*0.3;
            titulo.setY(titulo.getY()+(float)y);
            angulo += 0.087/2;
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

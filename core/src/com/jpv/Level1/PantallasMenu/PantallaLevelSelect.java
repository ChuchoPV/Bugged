package com.jpv.Level1.PantallasMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jpv.Level1.Level1.Level1;
import com.jpv.Level1.Level1.Tools.GenericButton;
import com.jpv.Level1.PantallasMenu.Tools.Pantalla;

public class PantallaLevelSelect extends Pantalla {
    private final Bugged pantallaInicio;
    private Texture fondoLevelaSelect;
    private Stage escenaLevelSelect;

    public PantallaLevelSelect(Bugged pantallaInicio) {
        this.pantallaInicio = pantallaInicio;
    }

    @Override
    public void show() {
        crearEscena();
        fondoLevelaSelect = new Texture("Level_Select/Level_Select.png");
        Gdx.input.setInputProcessor(escenaLevelSelect);
    }
    private void crearEscena() {
        escenaLevelSelect = new Stage(vista);

        //Boton city
        GenericButton btnCity =  new GenericButton(ANCHO / 6-150, ALTO / 2-20,"Level_Select/city.png","Level_Select/city_pressed.png");
        btnCity.button().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                new Level1(pantallaInicio);
                }
            }
        );

        //Boton Suburbs
        GenericButton btnSuburbs =  new GenericButton(ANCHO / 6+250, ALTO / 2-20,"Level_Select/suburbs.png","Level_Select/suburbs_pressed.png");
        btnSuburbs.button().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                }
            }
        );

        //Boton Mountain
        GenericButton btnMountain =  new GenericButton(ANCHO / 6+650, ALTO / 2-20,"Level_Select/mountain.png","Level_Select/mountain_pressed.png");
        btnMountain.button().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                }
            }
        );

        //Boton Cave
        GenericButton btnCave =  new GenericButton(ANCHO / 6+40, ALTO / 4-70,"Level_Select/cave.png","Level_Select/cave_pressed.png");
        btnCave.button().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                }
            }
        );

        //Boton Boss
        GenericButton btnBoss =  new GenericButton(ANCHO / 6+470, ALTO / 4-70,"Level_Select/Boss.png","Level_Select/Boss_Pressed.png");
        btnBoss.button().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                }
            }
        );

        //Boton back
        GenericButton btnBack = new GenericButton(ANCHO, ALTO,"PrincipalScreen/back_btn.png","PrincipalScreen/back_btn_pressed.png");
        btnBack.setPlace(ANCHO-btnBack.button().getWidth(),ALTO-btnBack.button().getHeight()-25);
        btnBack.button().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                }
            }
        );

        //Boton Character Selecton
        GenericButton btnCS = new GenericButton(ANCHO, ALTO,"Level_Select/CharacterSelect_Btn.png","Level_Select/CharacterSelect_Btn_pressed.png");
        btnCS.setPlace(btnBack.button().getWidth()-80,150);
        btnCS.button().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                pantallaInicio.setScreen(new PantallaCharacterSelection(pantallaInicio));
                }
            }
        );

        escenaLevelSelect.addActor(btnCity.button());
        escenaLevelSelect.addActor(btnSuburbs.button());
        escenaLevelSelect.addActor(btnMountain.button());
        escenaLevelSelect.addActor(btnCave.button());
        escenaLevelSelect.addActor(btnBoss.button());
        escenaLevelSelect.addActor(btnBack.button());
        escenaLevelSelect.addActor(btnCS.button());


    }
    @Override
    public void render(float delta) {
        borrarPantalla(1,1,0.5f);
        batch.setProjectionMatrix((camara.combined));
        batch.begin();
        batch.draw(fondoLevelaSelect,0,0);
        batch.end();
        escenaLevelSelect.draw();

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
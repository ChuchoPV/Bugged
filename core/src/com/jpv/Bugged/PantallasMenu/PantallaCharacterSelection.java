package com.jpv.Bugged.PantallasMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jpv.Bugged.Niveles.LevelManager;
import com.jpv.Bugged.Niveles.Tools.GenericButton;
import com.jpv.Bugged.PantallasMenu.Tools.Pantalla;

public class PantallaCharacterSelection extends Pantalla {
    private final Bugged pantallaInicio;
    public Texture fondo;
    public Stage escenaCharacterSelection;
    public Boolean hank=true;

    public PantallaCharacterSelection(Bugged pantallaInicio) {
        this.pantallaInicio = pantallaInicio;
    }

    @Override
    public void show(){
        crearEscena();
        fondo = new Texture("CharacterSelectionScreen/Character_Select.png");
        Gdx.input.setInputProcessor(escenaCharacterSelection);

    }

    private void crearEscena() {
        escenaCharacterSelection = new Stage(vista);

        //Boton Bridgete
        Texture textBtnBrid = new Texture("CharacterSelectionScreen/Bridgete_Select_Button.png");
        TextureRegionDrawable trd= new TextureRegionDrawable(new TextureRegion(textBtnBrid));
        //imagenBotonOprimido
        Texture textBtnOprimidoBrid = new Texture("CharacterSelectionScreen/Bridgete_Select_Button_Pressed.png");
        TextureRegionDrawable trdOp= new TextureRegionDrawable(new TextureRegion(textBtnOprimidoBrid));
        ImageButton btnBrid = new ImageButton(trd, trdOp);
        btnBrid.setPosition(ANCHO/6, ALTO/4);
        //Acción del botón
        btnBrid.addListener(new ClickListener(){
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    super.clicked(event, x, y);
                                    pantallaInicio.setHank(false);
                                    //pantallaInicio.setScreen(new PantallaJuego(pantallaInicio));
                                }
                            }
        );

        //Boton Hank
        Texture textBtnHank = new Texture("CharacterSelectionScreen/Hank_Select_Button.png");
        TextureRegionDrawable trdHank= new TextureRegionDrawable(new TextureRegion(textBtnHank));
        //imagenBotonOprimido
        Texture textBtnOprimidoHank = new Texture("CharacterSelectionScreen/Hank_Select_Button_Pressed.png");
        TextureRegionDrawable trdOpHank = new TextureRegionDrawable(new TextureRegion(textBtnOprimidoHank));
        ImageButton btnHank = new ImageButton(trdHank, trdOpHank);
        btnHank.setPosition(ANCHO-(ANCHO/6+btnHank.getWidth()), ALTO/4);
        //Acción del botón
        btnHank.addListener(new ClickListener(){
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    super.clicked(event, x, y);
                                    pantallaInicio.setHank(true);
                                    //pantallaInicio.setScreen(new PantallaJuego(pantallaInicio));
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
                                             pantallaInicio.setScreen(new PantallaMenuPrincipal(pantallaInicio));
                                         }
                                     }
        );

        escenaCharacterSelection.addActor(btnBack.button());
        escenaCharacterSelection.addActor(btnBrid);
        escenaCharacterSelection.addActor(btnHank);
    }


    @Override
    public void render(float delta) {
        borrarPantalla(1,1,0.5f);
        batch.setProjectionMatrix((camara.combined));
        batch.begin();
        batch.draw(fondo,0,0);
        batch.end();
        escenaCharacterSelection.draw();
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

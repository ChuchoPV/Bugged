package com.jpv.Bugged.PantallasMenu.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.jpv.Bugged.Niveles.Tools.GenericButton;
import com.jpv.Bugged.PantallasMenu.Bugged;

public class PantallaIntro extends Pantalla {

    private final Bugged pantallaInicio;
    private Stage escena;
    private Texture fondo;
    private int clicks;
    private boolean isHank;
    private Animation boom;
    private float statetimer;

    public PantallaIntro(Bugged pantallaInicio, int clicks) {
        this.pantallaInicio = pantallaInicio;
        this.clicks = clicks;
        statetimer = 0;
    }

    @Override
    public void show() {
        crearEscena();
        fondo = new Texture("vacia.png");
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(new Texture("Historia Final/Historia_3.png")));
        frames.add(new TextureRegion(new Texture("Historia Final/Historia_4.png")));
        frames.add(new TextureRegion(new Texture("Historia Final/Historia_5.png")));
        frames.add(new TextureRegion(new Texture("Historia Final/Historia_6.png")));
        frames.add(new TextureRegion(new Texture("Historia Final/Historia_7.png")));
        frames.add(new TextureRegion(new Texture("Historia Final/Historia_8.png")));
        frames.add(new TextureRegion(new Texture("Historia Final/Historia_9.png")));

        boom = new Animation<TextureRegion>(.1f, frames);
        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void crearEscena() {
        escena = new Stage(vista);
        GenericButton btnBob;
        switch (clicks){
            case(0):
                btnBob = new GenericButton(0, 0, "Historia Final/Hisotria 1.png", "Historia Final/Hisotria 1.png");
                btnBob.button().addListener(new ClickListener() {
                                                @Override
                                                public void clicked(InputEvent event, float x, float y) {
                                                    super.clicked(event, x, y);

                                                    pantallaInicio.setScreen(new PantallaIntro(pantallaInicio, 1));
                                                }
                                            }
                );
                escena.addActor(btnBob.button());

                break;
            case(1):
                btnBob = new GenericButton(0, 0, "Historia Final/Hisotria 2.png", "Historia Final/Hisotria 2.png");
                btnBob.button().addListener(new ClickListener() {
                                                @Override
                                                public void clicked(InputEvent event, float x, float y) {
                                                    super.clicked(event, x, y);

                                                    pantallaInicio.setScreen(new PantallaIntro(pantallaInicio, 2));
                                                }
                                            }
                );
                escena.addActor(btnBob.button());

                break;
            case(2):
                //btnBob = new GenericButton(0,0, "vacia.png", "vacia.png");
               /* btnBob = new GenericButton(0, 0, "Historia Final/Historia_9.png", "Historia Final/Historia_9.png");
                btnBob.button().addListener(new ClickListener() {
                                                @Override
                                                public void clicked(InputEvent event, float x, float y) {
                                                    super.clicked(event, x, y);
                                                    pantallaInicio.setScreen(new PantallaIntro(pantallaInicio, isHank, 3));
                                                }
                                            }
                );*/
                break;
            case(3):
                btnBob = new GenericButton(0, 0, "Historia Final/Historia_10.png", "Historia Final/Historia_10.png");
                btnBob.button().addListener(new ClickListener() {
                                                @Override
                                                public void clicked(InputEvent event, float x, float y) {
                                                    super.clicked(event, x, y);

                                                    pantallaInicio.setScreen(new PantallaIntro(pantallaInicio, 4));
                                                }
                                            }
                );
                escena.addActor(btnBob.button());

                break;
            case(4):
                btnBob = new GenericButton(0, 0, "Historia Final/Historia_11.png", "Historia Final/Historia_11.png");
                btnBob.button().addListener(new ClickListener() {
                                                @Override
                                                public void clicked(InputEvent event, float x, float y) {
                                                    super.clicked(event, x, y);

                                                    pantallaInicio.setScreen(new PantallaIntro(pantallaInicio, 5));
                                                }
                                            }
                );
                escena.addActor(btnBob.button());

                break;
            case(5):
                btnBob = new GenericButton(0, 0, "Historia Final/Historia_12.png", "Historia Final/Historia_12.png");
                btnBob.button().addListener(new ClickListener() {
                                                @Override
                                                public void clicked(InputEvent event, float x, float y) {
                                                    super.clicked(event, x, y);

                                                    pantallaInicio.setScreen(new PantallaCarga(pantallaInicio,1));
                                                }
                                            }
                );
                escena.addActor(btnBob.button());

                break;
            default:
                btnBob = new GenericButton(0, 0, "Historia Final/Historia_12.png", "Historia Final/Historia_12.png");
                btnBob.button().addListener(new ClickListener() {
                                                @Override
                                                public void clicked(InputEvent event, float x, float y) {
                                                    super.clicked(event, x, y);

                                                    pantallaInicio.setScreen(new PantallaCarga(pantallaInicio,1));
                                                }
                                            }
                );
                escena.addActor(btnBob.button());

        }
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix((camara.combined));
        statetimer += delta;
        batch.begin();
        batch.draw(fondo,0,0);
        if(clicks == 2) {
            batch.draw((TextureRegion) (boom.getKeyFrame(statetimer,false)), 0, 0);
        }
        batch.end();
        if(clicks != 2){
            escena.draw();
        }
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

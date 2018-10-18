package com.jpv.Level1.Level1.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jpv.Level1.Level1.Level1;

public class PauseScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Level1 game;
    private PlayScreen screen;

    public PauseScreen(PlayScreen screen) {
        this.screen = screen;
        this.game = screen.getGame();
        viewport = new FitViewport(Level1.V_WIDTH,Level1.V_HEIGHT,new OrthographicCamera());
    }

    private void crearEscena() {
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        Texture textBtn = new Texture("Pause_letters.png");
        TextureRegionDrawable trd = new TextureRegionDrawable(new TextureRegion(textBtn));
        ImageButton btn = new ImageButton(trd);
        btn.setPosition(Level1.V_WIDTH / Level1.PPM, Level1.V_HEIGHT / Level1.PPM);
        //Acción del botón
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.getHud().setStage();
                game.getPantallaInicio().setScreen(screen);
            }
        }
        );

        stage.addActor(btn);
    }

    @Override
    public void show() {
        crearEscena();
    }

    @Override
    public void render(float delta) {

        stage.draw();
    }

    public void setInputProcessor(){
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

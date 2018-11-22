package mx.itesm.Bugged.Niveles.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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
import mx.itesm.Bugged.Niveles.LevelManager;
import mx.itesm.Bugged.Niveles.Tools.GenericButton;
import mx.itesm.Bugged.PantallasMenu.PantallaLevelSelect;
import mx.itesm.Bugged.PantallasMenu.PantallaMenuPrincipal;

public class WinnerScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private PlayScreen screen;
    private LevelManager game;

    public WinnerScreen(PlayScreen screen){
        this.screen = screen;
        this.game = screen.getGame();
        viewport = new FitViewport(LevelManager.V_WIDTH, LevelManager.V_HEIGHT,new OrthographicCamera());
    }

    private void crearEscena() {
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        Texture textBtn = new Texture("LevelComplete.png");
        TextureRegionDrawable trd = new TextureRegionDrawable(new TextureRegion(textBtn));
        ImageButton btn = new ImageButton(trd);
        btn.setPosition(LevelManager.V_WIDTH / LevelManager.PPM, LevelManager.V_HEIGHT / LevelManager.PPM);
        btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    screen.getHud().setInputProcessor();
                    game.getPantallaInicio().setScreen(new PantallaLevelSelect(game.getPantallaInicio()));
                    dispose();
                }
            }
        );

        GenericButton btnBack = new GenericButton(LevelManager.V_WIDTH / LevelManager.PPM, (LevelManager.V_HEIGHT / LevelManager.PPM) + 600,"PrincipalScreen/back_btn.png","PrincipalScreen/back_btn_pressed.png");
        btnBack.button().addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent event, float x, float y) {
                 super.clicked(event, x, y);
                 screen.getGame().getPantallaInicio().setScreen(new PantallaLevelSelect(screen.getGame().getPantallaInicio()));
             }
         }
        );

        GenericButton btnHome = new GenericButton((LevelManager.V_WIDTH / LevelManager.PPM) + 1150, (LevelManager.V_HEIGHT / LevelManager.PPM) + 600,"home.png","home.png");
        btnHome.button().addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent event, float x, float y) {
                 super.clicked(event, x, y);
                 screen.getGame().getPantallaInicio().setScreen(new PantallaMenuPrincipal(screen.getGame().getPantallaInicio()));
             }
         }
        );


        stage.addActor(btn);
        stage.addActor(btnBack.button());
        stage.addActor(btnHome.button());
    }

    @Override
    public void show() {
        crearEscena();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
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

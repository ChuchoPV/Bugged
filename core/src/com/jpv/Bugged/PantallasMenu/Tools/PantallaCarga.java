package com.jpv.Bugged.PantallasMenu.Tools;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.jpv.Bugged.Niveles.LevelManager;
import com.jpv.Bugged.PantallasMenu.Bugged;


public class PantallaCarga extends Pantalla{
    private Bugged pantallainicio;
    private int level;
    private Animation<TextureRegion> loading;
    private float statetimer;
    //private Animation<TextureRegion> idle;

    public PantallaCarga(Bugged pantalllainicio, int level){
        this.pantallainicio = pantalllainicio;
        this.level = level;
        statetimer = 0;
    }

    @Override
    public void show() {
        Array<TextureRegion> temp = new Array<TextureRegion>();
        /*
        TextureAtlas atlas = manager.get("ATLAS_Final.pack",TextureAtlas.class);
        for(int i = 0; i < 8; i++) {
            temp.add(new TextureRegion(atlas.findRegion("mosquito_idle"), i * 160, 0, 160, 160));
        }
        idle = new Animation<TextureRegion>(0.1f, temp);
        temp.clear();*/

        temp.add(new TextureRegion(new Texture("Loading/Loading1.png")));
        temp.add(new TextureRegion(new Texture("Loading/Loading2.png")));
        temp.add(new TextureRegion(new Texture("Loading/Loading3.png")));

        loading = new Animation<TextureRegion>(0.2f,temp);

        //pantallainicio.getManager().load("ATLAS_Final.pack", TextureAtlas.class);
        pantallainicio.getManager().load("ATLAS_Final_2.pack", TextureAtlas.class);

        pantallainicio.getManager().setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        if(level == 1){
            pantallainicio.getManager().load("music/Scarf & Chocolates.mp3", Music.class);
            pantallainicio.getManager().load("City_Map.tmx", TiledMap.class);
        }else if(level == 2){
            pantallainicio.getManager().load("Subs_Map.tmx",TiledMap.class);
            pantallainicio.getManager().load("music/Sinner.mp3", Music.class);
        }else if(level == 3){
            pantallainicio.getManager().load("Mountain_Map.tmx",TiledMap.class);
            pantallainicio.getManager().load("music/Piano Lesson.mp3", Music.class);
        }else if(level == 4){
            pantallainicio.getManager().load("Cave_Map.tmx",TiledMap.class);
            pantallainicio.getManager().load("music/Sofa Memorabilida (kumatora).mp3", Music.class);
        }else{
            pantallainicio.getManager().load("Final.tmx",TiledMap.class);
            pantallainicio.getManager().load("music/Coconut Water.mp3", Music.class);
        }

        //manager.finishLoading();

    }

    private void actualizarCarga(){
        if (pantallainicio.getManager().update()){
            if(level==1){
                this.dispose();
                new LevelManager(pantallainicio, 1);
            }else if(level == 2){
                new LevelManager(pantallainicio, 2);
                this.dispose();
            }else if(level == 3){
                new LevelManager(pantallainicio, 3);
                this.dispose();
            }else if(level == 4){
                new LevelManager(pantallainicio, 4);
                this.dispose();
            }else if(level==5){
                new LevelManager(pantallainicio, 5);
                this.dispose();
            }
        } else {
            //Aún no termina, preguntar cómo va
            float avance = pantallainicio.getManager().getProgress();
        }
    }

    @Override
    public void render(float delta) {
        actualizarCarga();
        statetimer += delta;
        borrarPantalla();
        batch.setProjectionMatrix((camara.combined));
        batch.begin();
        batch.draw(loading.getKeyFrame(statetimer,true),0,0);
        //batch.draw(idle.getKeyFrame(statetimer,true),(ANCHO / 3) + 80,ALTO / 3);
        batch.end();
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

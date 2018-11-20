package com.jpv.Bugged.Niveles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jpv.Bugged.Niveles.Screens.PlayScreen;
import com.jpv.Bugged.PantallasMenu.Bugged;


public class LevelManager extends Game{
    public static final float V_WIDTH = 1280;
    public static final float V_HEIGHT = 720;
    public static final float PPM = 100;

    public static final short GROUND_BIT = 1;
    public static final short CHARACTER_BIT = 2;
    public static final short CHARACTER_HEAD_BIT = 4;
    public static final short CHARACTER_ARMA_BIT = 8;
    public static final short PLATAFORM_BIT = 16;
    public static final short OBSTACULE_BIT = 32;
    public static final short OBJECT_BIT = 64;
    public static final short ENEMY_BIT = 128;
    public static final short SHOTTER_CONTACT= 256;
    public static final short ITEM_BIT = 512;
    public static final short BOSS_BIT = 1024;
    public static final short BOSS_PIES_BIT = 2048;
    public static final short BOSS_COLLIDER_BIT = 4096;
    public static final short ENEMY_PROYECT = 8192;
    public static final short CHARACTER_PROYECT = 16384;

    public SpriteBatch batch;
    private Bugged pantallaInicio;
    private int level;
    private boolean isHank;
    private AssetManager manager;

    public LevelManager(Bugged pantallaincio, int level, boolean isHank) {
        this.level = level;
        batch = new SpriteBatch();
        this.pantallaInicio = pantallaincio;
        this.isHank = isHank;
        manager = new AssetManager();
        manager.load("music/Coconut Water.mp3", Music.class);
        manager.load("music/Piano Lesson.mp3", Music.class);
        manager.load("music/Scarf & Chocolates.mp3", Music.class);
        manager.load("music/Sinner.mp3", Music.class);
        manager.load("music/Sofa Memorabilida (kumatora).mp3", Music.class);
        manager.finishLoading();
        create();
	}

    public void create() {
        pantallaInicio.setScreen(new PlayScreen(this,level));
    }

    public Bugged getPantallaInicio() {
        return pantallaInicio;
    }
    public boolean isHank() {
        return isHank;
    }
    public AssetManager getManager() {
        return manager;
    }

    @Override
    public void dispose() {
        super.dispose();
        manager.dispose();
        batch.dispose();
    }
}

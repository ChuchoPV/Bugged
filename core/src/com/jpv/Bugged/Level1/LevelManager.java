package com.jpv.Bugged.Level1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jpv.Bugged.Level1.Screens.PlayScreen;
import com.jpv.Bugged.PantallasMenu.Bugged;


public class LevelManager extends Game{
    public static final float V_WIDTH = 1280;
    public static final float V_HEIGHT = 720;
    public static final float PPM = 100;

    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short CHARACTER_BIT = 2;
    public static final short CHARACTER_HEAD_BIT = 4;
    public static final short CHARACTER_ARMA_BIT = 8;
    public static final short PLATAFORM_BIT = 16;
    public static final short OBSTACULE_BIT = 32;
    public static final short OBJECT_BIT = 64;
    public static final short ENEMY_BIT = 128;
    public static final short ENEMY_COLLIDER_BIT = 256;
    public static final short ITEM_BIT = 512;
    public static final short BOSS_BIT = 1024;
    public static final short BOSS_PIES_BIT = 2048;
    public static final short BOSS_COLLIDER_BIT = 4096;


    public SpriteBatch batch;
    private Bugged pantallaInicio;
    private int level;

    public LevelManager(Bugged pantallaincio, int level) {
        this.level = level;
        batch = new SpriteBatch();
        this.pantallaInicio = pantallaincio;
        create();
	}

    public void create() {
        pantallaInicio.setScreen(new PlayScreen(this,level));
    }

    public Bugged getPantallaInicio() {
        return pantallaInicio;
    }

}

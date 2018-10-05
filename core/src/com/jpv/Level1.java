package com.jpv;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jpv.Screens.PlayScreen;

public class Level1 extends Game{
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
    public static final short DESTROYED_BIT = 64;
    public static final short OBJECT_BIT = 128;
    public static final short ENEMY_BIT = 256;
    public static final short ENEMY_COLLIDER_BIT = 512;
    public static final short ITEM_BIT = 1024;

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

    @Override
    public void render() { super.render(); }
}

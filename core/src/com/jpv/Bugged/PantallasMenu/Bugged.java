package com.jpv.Bugged.PantallasMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class Bugged extends Game {
    private boolean isHank=true;
    private boolean soundIsOn=true;
    private boolean musicIsOn=true;
    private AssetManager manager = new AssetManager();

    @Override
    public void create () {
        setScreen(new PantallaMenuPrincipal(this));
    }

    public void setHank(boolean bool){
        this.isHank=bool;
    }
    public boolean isHank() {
        return isHank;
    }

    public void setSound(boolean bool){
        this.soundIsOn=bool;
    }
    public boolean soundIsOn() {
        return soundIsOn;
    }

    public void setMusic(boolean bool){
        this.musicIsOn=bool;
    }
    public boolean musicIsOn() {
        return musicIsOn;
    }

    public void setManager(AssetManager manager){
        this.manager = manager;
    }
    public AssetManager getManager() {
        return manager;
    }
}


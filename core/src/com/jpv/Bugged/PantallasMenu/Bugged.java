package com.jpv.Bugged.PantallasMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Bugged extends Game {
    private boolean isHank=true;
    private boolean soundIsOn=true;
    private boolean musicIsOn=true;
    private AssetManager manager = new AssetManager();
    private boolean historyFirst = true;

    @Override
    public void create () {
        manager.load("ATLAS_Final.pack", TextureAtlas.class);
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
    public AssetManager getManager() {
        return manager;
    }

    public void setHistoryFirst(boolean first){
        this.historyFirst = first;
    }
    public boolean isHistoryFirst() {
        return historyFirst;
    }
}


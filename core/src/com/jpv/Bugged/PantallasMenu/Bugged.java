package com.jpv.Bugged.PantallasMenu;

import com.badlogic.gdx.Game;

public class Bugged extends Game {
    private boolean isHank=true;

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
}


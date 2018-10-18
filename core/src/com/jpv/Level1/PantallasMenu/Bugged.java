package com.jpv.Level1.PantallasMenu;

import com.badlogic.gdx.Game;

public class Bugged extends Game {

    @Override
    public void create () {
        setScreen(new PantallaMenuPrincipal(this));
    }

}


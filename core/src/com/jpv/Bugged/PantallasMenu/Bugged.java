package com.jpv.Bugged.PantallasMenu;

import com.badlogic.gdx.Game;

public class Bugged extends Game {

    @Override
    public void create () {
        setScreen(new PantallaMenuPrincipal(this));
    }

}


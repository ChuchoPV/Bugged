package com.jpv.Level1.PantallasMenu;

import com.badlogic.gdx.Game;

public class PantallaInicio extends Game {

    @Override
    public void create () {
        setScreen(new PantallaLevelSelect(this));
    }

}


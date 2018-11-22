package com.jpv.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import mx.itesm.Bugged.PantallasMenu.Bugged;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Bugged(), config);
		config.width = 1280;
		config.height = 720;


	}
}

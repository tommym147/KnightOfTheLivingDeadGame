package com.thomasmejia.knightofthelivingdead.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.thomasmejia.knightofthelivingdead.KnightOfTheLivingDead;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new KnightOfTheLivingDead(), config);
	}
}

package com.github.dennispronin.libdgxpong;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowIcon("icon.png");
		config.setTitle("Pong");
		config.setResizable(false);
		config.setWindowedMode(700, 400);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new LibgdxPong(), config);
	}
}

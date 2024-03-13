package com.github.dennispronin.libdgxpong;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.dennispronin.libdgxpong.multiplayer.client.PongGame;

import static com.github.dennispronin.libdgxpong.Constants.WINDOW_HEIGHT;
import static com.github.dennispronin.libdgxpong.Constants.WINDOW_WIDTH;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowIcon("icon.png");
        config.setTitle("Pong");
        config.setResizable(false);
        config.setWindowedMode((int) WINDOW_WIDTH, (int) WINDOW_HEIGHT);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new PongGame(), config);
    }
}

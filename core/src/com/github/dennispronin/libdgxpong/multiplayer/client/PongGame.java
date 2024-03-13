package com.github.dennispronin.libdgxpong.multiplayer.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.dennispronin.libdgxpong.multiplayer.client.screen.InitialScreen;

public class PongGame extends Game {

    private SpriteBatch batch;
    private BitmapFont font;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new InitialScreen());
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
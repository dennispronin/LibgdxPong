package com.github.dennispronin.libdgxpong.multiplayer.example.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Client;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.screen.InitialScreen;

public class PongGame extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    private Client client;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new InitialScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
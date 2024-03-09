package com.github.dennispronin.libdgxpong.multiplayer.example.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.screen.GameScreen;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.response.StartRoundEvent;

public class EventListener extends Listener {

    @Override
    public void received(Connection connection, final Object object) {
        if (object instanceof StartRoundEvent) {
            StartRoundEvent startRoundEvent = (StartRoundEvent) object;
            int leftPlayerScore = startRoundEvent.getLeftPlayerScore();
            int rightPlayerScore = startRoundEvent.getRightPlayerScore();
            float ballInitialX = startRoundEvent.getBallInitialX();
            float ballInitialY = startRoundEvent.getBallInitialY();
            Gdx.app.postRunnable(() -> {
                PongGame pongGame = ((PongGame) Gdx.app.getApplicationListener());
                if (pongGame.getScreen().getClass() != GameScreen.class) {
                    pongGame.setScreen(new GameScreen(leftPlayerScore, rightPlayerScore, ballInitialX, ballInitialY));
                } else {
                    GameScreen gameScreen = (GameScreen) pongGame.getScreen();
                    gameScreen.setLeftPlayerScore(leftPlayerScore);
                    gameScreen.setRightPlayerScore(rightPlayerScore);
                    gameScreen.setBallInitialX(ballInitialX);
                    gameScreen.setBallInitialY(ballInitialY);
                }
            });
        }
        super.received(connection, object);
    }
}
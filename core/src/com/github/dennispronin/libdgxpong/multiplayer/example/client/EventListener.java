package com.github.dennispronin.libdgxpong.multiplayer.example.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.screen.GameScreen;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.events.MoveRectangleServerEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.events.StartRoundServerEvent;

public class EventListener extends Listener {

    @Override
    public void received(Connection connection, final Object object) {
        if (object instanceof StartRoundServerEvent) {
            handleStartRoundEvent((StartRoundServerEvent) object, connection);
        }
        if (object instanceof MoveRectangleServerEvent) {
            handleMoveRectangleEvent((MoveRectangleServerEvent) object);
        }
        super.received(connection, object);
    }

    private void handleStartRoundEvent(StartRoundServerEvent startRoundServerEvent, Connection connection) {
        int leftPlayerScore = startRoundServerEvent.getLeftPlayerScore();
        int rightPlayerScore = startRoundServerEvent.getRightPlayerScore();
        float ballInitialX = startRoundServerEvent.getBallInitialX();
        float ballInitialY = startRoundServerEvent.getBallInitialY();
//            Gdx.app.postRunnable(() -> {
        PongGame pongGame = ((PongGame) Gdx.app.getApplicationListener());
        if (pongGame.getScreen().getClass() != GameScreen.class) {
            pongGame.setScreen(new GameScreen(
                    leftPlayerScore,
                    rightPlayerScore,
                    ballInitialX,
                    ballInitialY,
                    startRoundServerEvent.getPlayerSide(),
                    connection,
                    startRoundServerEvent.getSessionId()
            ));
        } else {
            GameScreen gameScreen = (GameScreen) pongGame.getScreen();
            gameScreen.setLeftPlayerScore(leftPlayerScore);
            gameScreen.setRightPlayerScore(rightPlayerScore);
            gameScreen.setBallInitialX(ballInitialX);
            gameScreen.setBallInitialY(ballInitialY);
            gameScreen.resetBall();
        }
//            });
    }

    private void handleMoveRectangleEvent(MoveRectangleServerEvent moveRectangleServerEvent) {
        PongGame pongGame = ((PongGame) Gdx.app.getApplicationListener());
        GameScreen gameScreen = (GameScreen) pongGame.getScreen();
        gameScreen.moveOtherPlayer(moveRectangleServerEvent.getRectangleY());
    }
}
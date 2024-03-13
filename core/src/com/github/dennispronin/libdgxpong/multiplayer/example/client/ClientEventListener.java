package com.github.dennispronin.libdgxpong.multiplayer.example.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.screen.GameScreen;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.screen.InitialScreen;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.events.*;

public class ClientEventListener extends Listener {

    @Override
    public void received(Connection connection, final Object object) {
        if (object instanceof StartRoundServerEvent) {
            handleStartRoundEvent((StartRoundServerEvent) object, connection);
        }
        if (object instanceof CreateSessionServerEvent) {
            handleCreateSessionServerEvent((CreateSessionServerEvent) object);
        }
        if (object instanceof MoveRectangleServerEvent) {
            handleMoveRectangleEvent((MoveRectangleServerEvent) object);
        }
        if (object instanceof PlayerDisconnectedServerEvent) {
            handlePlayerDisconnectedServerEvent();
        }
        if (object instanceof WrongSessionIdServerEvent) {
            handleWrongSessionIdServerEvent();
        }
        super.received(connection, object);
    }

    private void handleCreateSessionServerEvent(CreateSessionServerEvent createSessionServerEvent) {
        PongGame pongGame = ((PongGame) Gdx.app.getApplicationListener());
        InitialScreen initialScreen = (InitialScreen) pongGame.getScreen();
        initialScreen.showMessage("Waiting for another player");
        initialScreen.setSessionId(createSessionServerEvent.getSessionId());
    }

    private void handleStartRoundEvent(StartRoundServerEvent startRoundServerEvent, Connection connection) {
        int leftPlayerScore = startRoundServerEvent.getLeftPlayerScore();
        int rightPlayerScore = startRoundServerEvent.getRightPlayerScore();
        float ballInitialX = startRoundServerEvent.getBallInitialX();
        float ballInitialY = startRoundServerEvent.getBallInitialY();
        Gdx.app.postRunnable(() -> {
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
                gameScreen.startRound(leftPlayerScore, rightPlayerScore, ballInitialX, ballInitialY);
            }
        });
    }

    private void handleMoveRectangleEvent(MoveRectangleServerEvent moveRectangleServerEvent) {
        PongGame pongGame = ((PongGame) Gdx.app.getApplicationListener());
        if (pongGame.getScreen().getClass() == GameScreen.class) {
            GameScreen gameScreen = (GameScreen) pongGame.getScreen();
            gameScreen.moveOtherPlayer(moveRectangleServerEvent.getRectangleY());
        }
    }

    private void handlePlayerDisconnectedServerEvent() {
        Gdx.app.postRunnable(() -> Gdx.app.postRunnable(() -> {
            PongGame pongGame = ((PongGame) Gdx.app.getApplicationListener());
            pongGame.getScreen().dispose();
            pongGame.setScreen(new InitialScreen());
            ((InitialScreen) pongGame.getScreen()).showMessage("Other player disconnected");
        }));
    }

    private void handleWrongSessionIdServerEvent() {
        PongGame pongGame = ((PongGame) Gdx.app.getApplicationListener());
        ((InitialScreen) pongGame.getScreen()).showMessage("Provided session does not exist");
    }
}
package com.github.dennispronin.libdgxpong.multiplayer.example.server.events.handler;

import com.esotericsoftware.kryonet.Connection;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.ScoreHitClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.events.StartRoundServerEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.state.ServerState;

public class ScoreHitClientEventHandler implements EventHandler {

    @Override
    public void handleEvent(Object event, Connection connection, ServerState serverState) {
        ScoreHitClientEvent scoreHitClientEvent = (ScoreHitClientEvent) event;

        serverState.findSessionBySessionId(scoreHitClientEvent.getSessionId()).ifPresent(session -> {
            session.incrementScore(scoreHitClientEvent.getPlayerSide());
            float ballNextX = serverState.getRandomNumber();
            float ballNextY = serverState.getRandomNumber();
            StartRoundServerEvent startRoundServerEvent = new StartRoundServerEvent(
                    session.getSessionId(),
                    session.getLeftPlayerScore(),
                    session.getRightPlayerScore(),
                    ballNextX,
                    ballNextY
            );
            session.getHostPlayer().sendTCP(startRoundServerEvent);
            session.getGuestPlayer().sendTCP(startRoundServerEvent);
        });
    }
}
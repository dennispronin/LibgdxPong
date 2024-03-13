package com.github.dennispronin.libdgxpong.multiplayer.server.events.handler;

import com.esotericsoftware.kryonet.Connection;
import com.github.dennispronin.libdgxpong.multiplayer.server.events.JoinSessionClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.server.events.StartRoundServerEvent;
import com.github.dennispronin.libdgxpong.multiplayer.server.events.WrongSessionIdServerEvent;
import com.github.dennispronin.libdgxpong.multiplayer.server.state.GameSession;
import com.github.dennispronin.libdgxpong.multiplayer.server.state.PlayerSide;
import com.github.dennispronin.libdgxpong.multiplayer.server.state.ServerState;

import java.util.Optional;

public class JoinSessionClientEventHandler implements EventHandler {

    @Override
    public void handleEvent(Object event, Connection connection, ServerState serverState) {
        JoinSessionClientEvent joinSessionClientEvent = (JoinSessionClientEvent) event;

        Optional<GameSession> optionalSession = serverState.findSessionBySessionId(joinSessionClientEvent.getSessionId());
        if (!optionalSession.isPresent()) {
            connection.sendTCP(new WrongSessionIdServerEvent());
            return;
        }
        GameSession session = optionalSession.get();
        if (session.getGuestPlayer() != null) {
            return;
        }
        if (!session.getSessionPassword().equals(joinSessionClientEvent.getSessionPassword())) {
            return;
        }
        session.setGuestPlayer(connection);
        float ballNextX = serverState.getRandomNumber();
        float ballNextY = serverState.getRandomNumber();
        StartRoundServerEvent startRoundServerEvent = new StartRoundServerEvent(
                session.getSessionId(),
                session.getLeftPlayerScore(),
                session.getRightPlayerScore(),
                ballNextX,
                ballNextY
        );

        serverState.addNewPlayer(session.getGuestPlayer(), session.getSessionId());
        serverState.addNewPlayer(session.getHostPlayer(), session.getSessionId());

        startRoundServerEvent.setPlayerSide(PlayerSide.LEFT);
        session.getHostPlayer().sendTCP(startRoundServerEvent);
        startRoundServerEvent.setPlayerSide(PlayerSide.RIGHT);
        session.getGuestPlayer().sendTCP(startRoundServerEvent);
    }
}
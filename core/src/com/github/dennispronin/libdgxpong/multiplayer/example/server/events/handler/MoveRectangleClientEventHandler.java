package com.github.dennispronin.libdgxpong.multiplayer.example.server.events.handler;

import com.esotericsoftware.kryonet.Connection;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.MoveRectangleClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.events.MoveRectangleServerEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.state.ServerState;

public class MoveRectangleClientEventHandler implements EventHandler {

    @Override
    public void handleEvent(Object event, Connection connection, ServerState serverState) {
        MoveRectangleClientEvent moveRectangleClientEvent = (MoveRectangleClientEvent) event;

        serverState.findSessionBySessionId(moveRectangleClientEvent.getSessionId()).ifPresent(session -> {
            if (session.getHostPlayer() == connection) {
                session.getGuestPlayer().sendTCP(new MoveRectangleServerEvent(moveRectangleClientEvent.getRectangleY()));
            } else {
                session.getHostPlayer().sendTCP(new MoveRectangleServerEvent(moveRectangleClientEvent.getRectangleY()));
            }
        });
    }
}
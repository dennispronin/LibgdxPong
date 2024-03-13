package com.github.dennispronin.libdgxpong.multiplayer.server.events.handler;

import com.esotericsoftware.kryonet.Connection;
import com.github.dennispronin.libdgxpong.multiplayer.server.events.CreateSessionClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.server.events.CreateSessionServerEvent;
import com.github.dennispronin.libdgxpong.multiplayer.server.state.ServerState;

public class CreateSessionClientEventHandler implements EventHandler {

    @Override
    public void handleEvent(Object event, Connection connection, ServerState serverState) {
        CreateSessionClientEvent createSessionClientEvent = (CreateSessionClientEvent) event;
        if (createSessionClientEvent.getSessionPassword() == null) {
            return;
        }
        String sessionId = serverState.createSession(connection, createSessionClientEvent.getSessionPassword());
        CreateSessionServerEvent createResponse = new CreateSessionServerEvent(sessionId);
        connection.sendTCP(createResponse);
    }
}
package com.github.dennispronin.libdgxpong.multiplayer.example.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.github.dennispronin.libdgxpong.Network;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.CreateSessionClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.JoinSessionClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.MoveRectangleClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.ScoreHitClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.events.PlayerDisconnectedServerEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.events.handler.*;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.state.ServerState;

import java.io.IOException;
import java.util.HashMap;

import static com.github.dennispronin.libdgxpong.Network.SERVER_PORT;
import static java.util.Optional.ofNullable;

public class PongServer {

    private static final ServerState SERVER_STATE = new ServerState();
    private static final HashMap<String, EventHandler> EVENT_CLASS_TO_HANDLERS = new HashMap<>();

    public static void main(String[] args) throws IOException {
        EVENT_CLASS_TO_HANDLERS.put(JoinSessionClientEvent.class.toString(), new JoinSessionClientEventHandler());
        EVENT_CLASS_TO_HANDLERS.put(CreateSessionClientEvent.class.toString(), new CreateSessionClientEventHandler());
        EVENT_CLASS_TO_HANDLERS.put(ScoreHitClientEvent.class.toString(), new ScoreHitClientEventHandler());
        EVENT_CLASS_TO_HANDLERS.put(MoveRectangleClientEvent.class.toString(), new MoveRectangleClientEventHandler());
        new PongServer();
    }

    public PongServer() throws IOException {
        Server server = new Server();
        Network.register(server);
        server.addListener(new Listener() {

            @Override
            public void received(Connection connection, Object object) {
                ofNullable(EVENT_CLASS_TO_HANDLERS.get(object.getClass().toString()))
                        .ifPresent(eventHandler -> eventHandler.handleEvent(object, connection, SERVER_STATE));
                super.received(connection, object);
            }

            @Override
            public void disconnected(Connection connection) {
                handlePlayerDisconnected(connection);
            }
        });
        server.bind(SERVER_PORT, SERVER_PORT);
        server.start();
    }

    private void handlePlayerDisconnected(Connection connection) {
        SERVER_STATE.findSessionByPlayer(connection).ifPresent(session -> {
            PlayerDisconnectedServerEvent playerDisconnectedServerEvent = new PlayerDisconnectedServerEvent();
            if (session.getGuestPlayer() == connection) {
                session.getHostPlayer().sendTCP(playerDisconnectedServerEvent);
            } else {
                session.getGuestPlayer().sendTCP(playerDisconnectedServerEvent);
            }
            SERVER_STATE.killSession(session.getSessionId());
        });
    }
}
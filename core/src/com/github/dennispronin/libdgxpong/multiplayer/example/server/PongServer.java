package com.github.dennispronin.libdgxpong.multiplayer.example.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.github.dennispronin.libdgxpong.Network;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.PlayerSide;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.CreateSessionClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.JoinSessionClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.MoveRectangleClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.ScoreHitClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.events.*;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.state.GameSession;

import java.io.IOException;
import java.util.*;

import static com.github.dennispronin.libdgxpong.Network.SERVER_PORT;

public class PongServer {

    private final Random random = new Random();
    private final Map<String, GameSession> gameSessions = new HashMap<>();
    private final Map<Connection, String> playersToGameSessionsId = new HashMap<>();

    public static void main(String[] args) throws IOException {
        new PongServer();
    }

    public PongServer() throws IOException {
        Server server = new Server();
        Network.register(server);
        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof JoinSessionClientEvent) {
                    handleJoinSessionEvent((JoinSessionClientEvent) object, connection);
                }
                if (object instanceof CreateSessionClientEvent) {
                    handleCreateSessionClientEvent((CreateSessionClientEvent) object, connection);
                }
                if (object instanceof ScoreHitClientEvent) {
                    handleScoreHitClientEvent((ScoreHitClientEvent) object);
                }
                if (object instanceof MoveRectangleClientEvent) {
                    handleMoveRectangleClientEvent((MoveRectangleClientEvent) object, connection);
                }
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
        String sessionId = playersToGameSessionsId.get(connection);
        Optional.ofNullable(gameSessions.get(sessionId)).ifPresent(session -> {
            PlayerDisconnectedServerEvent playerDisconnectedServerEvent = new PlayerDisconnectedServerEvent();
            if (session.getGuestPlayer() == connection) {
                session.getHostPlayer().sendTCP(playerDisconnectedServerEvent);
            } else {
                session.getGuestPlayer().sendTCP(playerDisconnectedServerEvent);
            }
            gameSessions.remove(sessionId);
            playersToGameSessionsId.remove(session.getHostPlayer());
            playersToGameSessionsId.remove(session.getGuestPlayer());
        });
    }

    private void handleMoveRectangleClientEvent(MoveRectangleClientEvent moveRectangleClientEvent, Connection connection) {
        Optional.ofNullable(gameSessions.get(moveRectangleClientEvent.getSessionId())).ifPresent(session -> {
            if (session.getHostPlayer() == connection) {
                session.getGuestPlayer().sendTCP(new MoveRectangleServerEvent(moveRectangleClientEvent.getRectangleY()));
            } else {
                session.getHostPlayer().sendTCP(new MoveRectangleServerEvent(moveRectangleClientEvent.getRectangleY()));
            }
        });
    }

    private void handleScoreHitClientEvent(ScoreHitClientEvent scoreHitClientEvent) {
        Optional.ofNullable(gameSessions.get(scoreHitClientEvent.getSessionId())).ifPresent(session -> {
            session.incrementScore(scoreHitClientEvent.getPlayerSide());
            float ballNextX = random.nextInt(2);
            float ballNextY = random.nextInt(2);
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

    private void handleCreateSessionClientEvent(CreateSessionClientEvent createSessionClientEvent, Connection connection) {
        if (createSessionClientEvent.getSessionPassword() == null) {
            return;
        }
        GameSession session = new GameSession();
        String sessionId = UUID.randomUUID().toString().substring(0, 8);
        while (gameSessions.containsKey(sessionId)) {
            sessionId = UUID.randomUUID().toString();
        }
        session.setSessionId(sessionId);
        session.setSessionPassword(createSessionClientEvent.getSessionPassword());
        session.setHostPlayer(connection);

        gameSessions.put(sessionId, session);
        CreateSessionServerEvent createResponse = new CreateSessionServerEvent(sessionId);
        connection.sendTCP(createResponse);
    }

    private void handleJoinSessionEvent(JoinSessionClientEvent joinSessionClientEvent, Connection connection) {
        GameSession session = gameSessions.get(joinSessionClientEvent.getSessionId());
        if (session != null) {
            if (session.getGuestPlayer() != null) {
                return;
            }
            if (!session.getSessionPassword().equals(joinSessionClientEvent.getSessionPassword())) {
                return;
            }
            session.setGuestPlayer(connection);
            float ballNextX = random.nextInt(2);
            float ballNextY = random.nextInt(2);
            StartRoundServerEvent startRoundServerEvent = new StartRoundServerEvent(
                    session.getSessionId(),
                    session.getLeftPlayerScore(),
                    session.getRightPlayerScore(),
                    ballNextX,
                    ballNextY
            );
            playersToGameSessionsId.put(session.getGuestPlayer(), session.getSessionId());
            playersToGameSessionsId.put(session.getHostPlayer(), session.getSessionId());

            startRoundServerEvent.setPlayerSide(PlayerSide.LEFT);
            session.getHostPlayer().sendTCP(startRoundServerEvent);
            startRoundServerEvent.setPlayerSide(PlayerSide.RIGHT);
            session.getGuestPlayer().sendTCP(startRoundServerEvent);
        } else {
            connection.sendTCP(new WrongSessionIdServerEvent());
        }
    }
}
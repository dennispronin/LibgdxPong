package com.github.dennispronin.libdgxpong.multiplayer.example.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.github.dennispronin.libdgxpong.Network;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.PlayerSide;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.request.CreateRequest;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.request.JoinRequest;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.request.ScoreEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.response.CreateResponse;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.response.StartRoundEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.state.GameSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.github.dennispronin.libdgxpong.Network.SERVER_PORT;

public class PongServer {

    private final Random random = new Random();
    private final Map<String, GameSession> gameSessions = new HashMap<>();

    public static void main(String[] args) throws IOException {
        new PongServer();
    }

    public PongServer() throws IOException {
        Server server = new Server();
        Network.register(server);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof JoinRequest) {
                    JoinRequest joinRequest = (JoinRequest) object;
                    GameSession session = gameSessions.get(joinRequest.getSessionId());
                    if (session == null) {
                        return;
                    }
                    if (session.getGuestPlayer() != null) {
                        return;
                    }
                    if (!session.getSessionPassword().equals(joinRequest.getSessionPassword())) {
                        return;
                    }
                    session.setGuestPlayer(connection);
                    float ballNextX = random.nextInt(2);
                    float ballNextY = random.nextInt(2);
                    StartRoundEvent startRoundEvent = new StartRoundEvent(
                            session.getSessionId(),
                            session.getLeftPlayerScore(),
                            session.getRightPlayerScore(),
                            ballNextX,
                            ballNextY
                    );

                    startRoundEvent.setPlayerSide(PlayerSide.LEFT);
                    session.getHostPlayer().sendTCP(startRoundEvent);
                    startRoundEvent.setPlayerSide(PlayerSide.RIGHT);
                    session.getGuestPlayer().sendTCP(startRoundEvent);
                }
                if (object instanceof CreateRequest) {
                    CreateRequest createRequest = (CreateRequest) object;
                    if (createRequest.getSessionPassword() == null) {
                        return;
                    }
                    GameSession session = new GameSession();
                    String sessionId = UUID.randomUUID().toString();
                    while (gameSessions.containsKey(sessionId)) {
                        sessionId = UUID.randomUUID().toString();
                    }
                    session.setSessionId(sessionId);
                    session.setSessionPassword(createRequest.getSessionPassword());
                    session.setHostPlayer(connection);

                    gameSessions.put(sessionId, session);
                    CreateResponse createResponse = new CreateResponse(sessionId);
                    connection.sendTCP(createResponse);
                }
                if (object instanceof ScoreEvent) {
                    ScoreEvent scoreEvent = (ScoreEvent) object;
                    GameSession session = gameSessions.get(scoreEvent.getSessionId());
                    session.incrementScore(scoreEvent.getPlayerSide());
                    float ballNextX = random.nextInt(2);
                    float ballNextY = random.nextInt(2);
                    StartRoundEvent startRoundEvent = new StartRoundEvent(
                            session.getSessionId(),
                            session.getLeftPlayerScore(),
                            session.getRightPlayerScore(),
                            ballNextX,
                            ballNextY
                    );
                    session.getHostPlayer().sendTCP(startRoundEvent);
                    session.getGuestPlayer().sendTCP(startRoundEvent);
                }
                super.received(connection, object);
            }
        });
        server.bind(SERVER_PORT);
        server.start();
    }
}
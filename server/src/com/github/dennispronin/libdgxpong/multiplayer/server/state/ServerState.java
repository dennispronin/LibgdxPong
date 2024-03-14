package com.github.dennispronin.libdgxpong.multiplayer.server.state;

import com.esotericsoftware.kryonet.Connection;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Optional.ofNullable;

public class ServerState {

    private final Random random = new Random();
    private final Map<String, GameSession> gameSessions = new HashMap<>();
    private final Map<Connection, String> playersToGameSessionsId = new HashMap<>();

    public ServerState() {
        startCleanSessionsLoop(Executors.newSingleThreadExecutor());
    }

    public int getRandomNumber() {
        return random.nextInt(2);
    }

    public String createSession(Connection hostPlayer, String sessionPassword) {
        GameSession session = new GameSession();
        String sessionId = UUID.randomUUID().toString().substring(0, 8);
        while (findSessionBySessionId(sessionId).isPresent()) {
            sessionId = UUID.randomUUID().toString();
        }

        session.setSessionId(sessionId);
        session.setSessionPassword(sessionPassword);
        session.setHostPlayer(hostPlayer);

        gameSessions.put(sessionId, session);
        return sessionId;
    }

    public void addNewPlayer(Connection playerConnection, String sessionId) {
        playersToGameSessionsId.put(playerConnection, sessionId);
    }

    public Optional<GameSession> findSessionBySessionId(String sessionId) {
        return ofNullable(gameSessions.get(sessionId));
    }

    public Optional<GameSession> findSessionByPlayer(Connection playerConnection) {
        return ofNullable(playersToGameSessionsId.get(playerConnection)).map(gameSessions::get);
    }

    public void killSession(String sessionId) {
        ofNullable(gameSessions.remove(sessionId))
                .ifPresent(gameSession -> {
                    playersToGameSessionsId.remove(gameSession.getHostPlayer());
                    playersToGameSessionsId.remove(gameSession.getGuestPlayer());
                });
    }

    public void startCleanSessionsLoop(ExecutorService executorService) {
        executorService.execute(() -> {
            while (true) {
                gameSessions.values()
                        .stream()
                        .filter(GameSession::isOldSession)
                        .forEach(session -> killSession(session.getSessionId()));
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
package com.github.dennispronin.libdgxpong.multiplayer.server.state;

import com.esotericsoftware.kryonet.Connection;

import java.time.LocalDateTime;

public class GameSession {

    private String sessionId;
    private String sessionPassword;
    private Connection hostPlayerConnection;
    private Connection guestPlayerConnection;
    private int leftPlayerScore = 0;
    private int rightPlayerScore = 0;

    private static final int SESSION_ALIVE_TIME_LIMIT_MINUTES = 60;
    private final LocalDateTime createDateTime = LocalDateTime.now();

    public void incrementScore(PlayerSide playerSide) {
        switch (playerSide) {
            case LEFT:
                leftPlayerScore++;
                break;
            case RIGHT:
                rightPlayerScore++;
                break;
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSessionPassword() {
        return sessionPassword;
    }

    public Connection getHostPlayer() {
        return hostPlayerConnection;
    }

    public Connection getGuestPlayer() {
        return guestPlayerConnection;
    }

    public int getLeftPlayerScore() {
        return leftPlayerScore;
    }

    public int getRightPlayerScore() {
        return rightPlayerScore;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setSessionPassword(String sessionPassword) {
        this.sessionPassword = sessionPassword;
    }

    public void setHostPlayer(Connection hostPlayerConnection) {
        this.hostPlayerConnection = hostPlayerConnection;
    }

    public void setGuestPlayer(Connection guestPlayerConnection) {
        this.guestPlayerConnection = guestPlayerConnection;
    }

    public boolean isOldSession() {
        return createDateTime.plusMinutes(SESSION_ALIVE_TIME_LIMIT_MINUTES).isBefore(LocalDateTime.now());
    }
}
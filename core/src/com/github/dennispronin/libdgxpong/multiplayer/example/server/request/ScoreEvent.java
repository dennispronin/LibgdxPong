package com.github.dennispronin.libdgxpong.multiplayer.example.server.request;

import com.github.dennispronin.libdgxpong.multiplayer.example.client.PlayerSide;

public class ScoreEvent {

    private final PlayerSide playerSide;
    private final String sessionId;

    public ScoreEvent(PlayerSide playerSide, String sessionId) {
        this.playerSide = playerSide;
        this.sessionId = sessionId;
    }

    public PlayerSide getPlayerSide() {
        return playerSide;
    }

    public String getSessionId() {
        return sessionId;
    }
}
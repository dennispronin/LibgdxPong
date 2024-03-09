package com.github.dennispronin.libdgxpong.multiplayer.example.client.events;

import com.github.dennispronin.libdgxpong.multiplayer.example.client.PlayerSide;

public class ScoreHitClientEvent {

    private final PlayerSide playerSide;
    private final String sessionId;

    public ScoreHitClientEvent(PlayerSide playerSide, String sessionId) {
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
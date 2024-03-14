package com.github.dennispronin.libdgxpong.multiplayer.server.event;

import com.github.dennispronin.libdgxpong.multiplayer.server.state.PlayerSide;

public class ScoreHitClientEvent {

    private PlayerSide playerSide;
    private String sessionId;

    public ScoreHitClientEvent() {}

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
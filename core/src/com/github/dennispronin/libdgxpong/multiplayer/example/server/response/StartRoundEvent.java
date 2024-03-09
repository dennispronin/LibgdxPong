package com.github.dennispronin.libdgxpong.multiplayer.example.server.response;

import com.github.dennispronin.libdgxpong.multiplayer.example.client.PlayerSide;

public class StartRoundEvent {

    private final String sessionId;
    private final int leftPlayerScore;
    private final int rightPlayerScore;
    private final float ballInitialX;
    private final float ballInitialY;
    private PlayerSide playerSide;

    public StartRoundEvent(String sessionId, int leftPlayerScore, int rightPlayerScore, float ballInitialX, float ballInitialY) {
        this.sessionId = sessionId;
        this.leftPlayerScore = leftPlayerScore;
        this.rightPlayerScore = rightPlayerScore;
        this.ballInitialX = ballInitialX;
        this.ballInitialY = ballInitialY;
    }

    public int getLeftPlayerScore() {
        return leftPlayerScore;
    }

    public int getRightPlayerScore() {
        return rightPlayerScore;
    }

    public float getBallInitialX() {
        return ballInitialX;
    }

    public float getBallInitialY() {
        return ballInitialY;
    }

    public void setPlayerSide(PlayerSide playerSide) {
        this.playerSide = playerSide;
    }

    public PlayerSide getPlayerSide() {
        return playerSide;
    }

    public String getSessionId() {
        return sessionId;
    }
}
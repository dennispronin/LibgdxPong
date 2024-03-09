package com.github.dennispronin.libdgxpong.multiplayer.example.server.events;

import com.github.dennispronin.libdgxpong.multiplayer.example.client.PlayerSide;

public class StartRoundServerEvent {

    private String sessionId;
    private int leftPlayerScore;
    private int rightPlayerScore;
    private float ballInitialX;
    private float ballInitialY;
    private PlayerSide playerSide;

    public StartRoundServerEvent() {
    }

    public StartRoundServerEvent(String sessionId, int leftPlayerScore, int rightPlayerScore, float ballInitialX, float ballInitialY) {
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
package com.github.dennispronin.libdgxpong.multiplayer.example.server.response;

public class StartRoundEvent {

    private final int leftPlayerScore;
    private final int rightPlayerScore;
    private final float ballInitialX;
    private final float ballInitialY;

    public StartRoundEvent(int leftPlayerScore, int rightPlayerScore, float ballInitialX, float ballInitialY) {
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
}
package com.github.dennispronin.libdgxpong.multiplayer.example.server.request;

public class MoveRectangleEvent {

    private final String sessionId;
    private final float rectangleY;

    public MoveRectangleEvent(String sessionId, float rectangleY) {
        this.sessionId = sessionId;
        this.rectangleY = rectangleY;
    }

    public String getSessionId() {
        return sessionId;
    }

    public float getRectangleY() {
        return rectangleY;
    }
}
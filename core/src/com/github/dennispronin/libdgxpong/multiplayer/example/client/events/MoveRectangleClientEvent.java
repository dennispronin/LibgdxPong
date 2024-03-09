package com.github.dennispronin.libdgxpong.multiplayer.example.client.events;

public class MoveRectangleClientEvent {

    private final String sessionId;
    private final float rectangleY;

    public MoveRectangleClientEvent(String sessionId, float rectangleY) {
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
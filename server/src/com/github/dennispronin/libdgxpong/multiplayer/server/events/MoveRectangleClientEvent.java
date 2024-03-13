package com.github.dennispronin.libdgxpong.multiplayer.server.events;

public class MoveRectangleClientEvent {

    private String sessionId;
    private float rectangleY;

    public MoveRectangleClientEvent() {}

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
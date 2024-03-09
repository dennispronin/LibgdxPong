package com.github.dennispronin.libdgxpong.multiplayer.example.server.response;

public class OtherPlayersMoveRectangleEvent {

    private final float rectangleY;

    public OtherPlayersMoveRectangleEvent(float rectangleY) {
        this.rectangleY = rectangleY;
    }

    public float getRectangleY() {
        return rectangleY;
    }
}
package com.github.dennispronin.libdgxpong.multiplayer.example.server.events;

public class MoveRectangleServerEvent {

    private final float rectangleY;

    public MoveRectangleServerEvent(float rectangleY) {
        this.rectangleY = rectangleY;
    }

    public float getRectangleY() {
        return rectangleY;
    }
}
package com.github.dennispronin.libdgxpong.multiplayer.server.events;

public class MoveRectangleServerEvent {

    private float rectangleY;

    public MoveRectangleServerEvent() {}

    public MoveRectangleServerEvent(float rectangleY) {
        this.rectangleY = rectangleY;
    }

    public float getRectangleY() {
        return rectangleY;
    }
}
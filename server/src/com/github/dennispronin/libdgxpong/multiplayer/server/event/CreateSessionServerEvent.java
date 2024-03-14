package com.github.dennispronin.libdgxpong.multiplayer.server.event;

public class CreateSessionServerEvent {

    private String sessionId;

    public CreateSessionServerEvent() {}

    public CreateSessionServerEvent(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
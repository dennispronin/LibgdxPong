package com.github.dennispronin.libdgxpong.multiplayer.example.server.response;

public class CreateResponse {
    private final String sessionId;

    public CreateResponse(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
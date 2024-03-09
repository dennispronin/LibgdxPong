package com.github.dennispronin.libdgxpong.multiplayer.example.server.request;

public class JoinRequest {

    private final String sessionPassword;
    private final String sessionId;

    public JoinRequest(String sessionPassword, String sessionId) {
        this.sessionPassword = sessionPassword;
        this.sessionId = sessionId;
    }

    public String getSessionPassword() {
        return sessionPassword;
    }

    public String getSessionId() {
        return sessionId;
    }
}
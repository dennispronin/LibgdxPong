package com.github.dennispronin.libdgxpong.multiplayer.example.client.events;

public class JoinSessionClientEvent {

    private String sessionPassword;
    private String sessionId;

    public JoinSessionClientEvent() {}

    public JoinSessionClientEvent(String sessionPassword, String sessionId) {
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
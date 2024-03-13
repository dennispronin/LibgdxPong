package com.github.dennispronin.libdgxpong.multiplayer.server.events;

public class CreateSessionClientEvent {

    private String sessionPassword;

    public CreateSessionClientEvent() {}

    public CreateSessionClientEvent(String sessionPassword) {
        this.sessionPassword = sessionPassword;
    }

    public String getSessionPassword() {
        return sessionPassword;
    }
}
package com.github.dennispronin.libdgxpong.multiplayer.example.client.events;

public class CreateSessionClientEvent {

    private final String sessionPassword;

    public CreateSessionClientEvent(String sessionPassword) {
        this.sessionPassword = sessionPassword;
    }

    public String getSessionPassword() {
        return sessionPassword;
    }
}
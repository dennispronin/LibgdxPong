package com.github.dennispronin.libdgxpong.multiplayer.example.server.request;

public class CreateRequest {

    private final String sessionPassword;

    public CreateRequest(String sessionPassword) {
        this.sessionPassword = sessionPassword;
    }

    public String getSessionPassword() {
        return sessionPassword;
    }
}
package com.github.dennispronin.libdgxpong.multiplayer.example.server.request;

public class CreateRequest {

    private String playerName;
    private String sessionPassword;

    public CreateRequest(String playerName, String sessionPassword) {
        this.playerName = playerName;
        this.sessionPassword = sessionPassword;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getSessionPassword() {
        return sessionPassword;
    }
}
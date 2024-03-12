package com.github.dennispronin.libdgxpong;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.PlayerSide;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.CreateSessionClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.JoinSessionClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.MoveRectangleClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.ScoreHitClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.events.*;

public class Network {
    public static final String SERVER_HOST = "localhost";

//    public static final String SERVER_HOST = "92.118.114.29";
    public static final int SERVER_PORT = 54555;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(CreateSessionClientEvent.class);
        kryo.register(JoinSessionClientEvent.class);
        kryo.register(MoveRectangleClientEvent.class);
        kryo.register(ScoreHitClientEvent.class);
        kryo.register(CreateSessionServerEvent.class);
        kryo.register(MoveRectangleServerEvent.class);
        kryo.register(StartRoundServerEvent.class);
        kryo.register(PlayerDisconnectedServerEvent.class);
        kryo.register(WrongSessionIdServerEvent.class);
        kryo.register(PlayerSide.class);
    }
}
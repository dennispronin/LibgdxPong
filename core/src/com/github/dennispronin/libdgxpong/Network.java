package com.github.dennispronin.libdgxpong;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.CreateSessionClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.JoinSessionClientEvent;

public class Network {

    public static final String SERVER_HOST = "92.118.114.29";
    public static final int SERVER_PORT = 54555;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(CreateSessionClientEvent.class);
        kryo.register(JoinSessionClientEvent.class);
    }
}
package com.github.dennispronin.libdgxpong;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.request.CreateRequest;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.request.JoinRequest;

public class Network {

    public static final String SERVER_HOST = "92.118.114.29";
    public static final int SERVER_PORT = 54555;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(CreateRequest.class);
        kryo.register(JoinRequest.class);
    }
}
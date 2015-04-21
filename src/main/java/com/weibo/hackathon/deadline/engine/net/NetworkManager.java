package com.weibo.hackathon.deadline.engine.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by axb on 15/4/17.
 */
public class NetworkManager {
    private ServerSocket server;
    private List<NetworkChannel> channels = new ArrayList<NetworkChannel>();

    public void open(int port) throws IOException {
        server = new ServerSocket(port);
    }

    public NetworkChannel nextChannel() throws IOException {
        Socket socket = server.accept();
        System.out.println(new Date() + "accept user from:"+socket.getRemoteSocketAddress());
        NetworkChannel channel = new NetworkChannel(socket);
        channels.add(channel);
        return channel;

    }

}

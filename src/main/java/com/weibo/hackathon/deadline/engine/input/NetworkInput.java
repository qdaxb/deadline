package com.weibo.hackathon.deadline.engine.input;

import com.weibo.hackathon.deadline.engine.net.NetworkChannel;
import com.weibo.hackathon.deadline.engine.net.NetworkManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

/**
 * Created by axb on 15/4/17.
 */
public class NetworkInput implements InputManager {


    private final NetworkChannel channel;

    public NetworkInput(NetworkChannel channel) throws IOException {
        this.channel = channel;
    }

    @Override
    public Set<GameInput> getInputStatus() {
        try {

            char[] data = channel.receive();
            System.out.println(new String(data));
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void close() {
    }
}

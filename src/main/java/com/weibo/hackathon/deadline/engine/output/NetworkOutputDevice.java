package com.weibo.hackathon.deadline.engine.output;

import java.io.IOException;

import com.weibo.hackathon.deadline.engine.net.NetworkChannel;

/**
 * Created by axb on 15/4/17.
 */
public class NetworkOutputDevice implements OutputDevice<String>{
    private final NetworkChannel channel;

    public NetworkOutputDevice(NetworkChannel channel) {
        this.channel = channel;
    }
    
    @Override
    public void output(String data) throws IOException {
        channel.send(data.toCharArray());
    }
    
    @Override
    public void outputRaw(String data) throws IOException {
        channel.sendRaw(data.toCharArray());
    }
}

package com.weibo.hackathon.deadline.engine.output;

import com.weibo.hackathon.deadline.engine.net.NetworkChannel;
import com.weibo.hackathon.deadline.engine.net.NetworkManager;

/**
 * Created by axb on 15/4/17.
 */
public class NetworkOutputDevice implements OutputDevice<String>{
    private final NetworkChannel channel;

    public NetworkOutputDevice(NetworkChannel channel) {
        this.channel = channel;
    }
    @Override
    public void output(String data) {
        channel.send(data.toCharArray());
        System.out.println("fff");

    }
}

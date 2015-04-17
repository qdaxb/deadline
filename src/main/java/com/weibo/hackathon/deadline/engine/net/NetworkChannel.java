package com.weibo.hackathon.deadline.engine.net;

import java.io.*;
import java.net.Socket;

/**
 * Created by axb on 15/4/17.
 */
public class NetworkChannel {
    private Socket socket;
    private Reader reader;
    private Writer writer;
    private StringBuffer buffer = new StringBuffer();
    public NetworkChannel(Socket socket) throws IOException {
        this.socket = socket;
        reader = new InputStreamReader(socket.getInputStream());
        writer = new OutputStreamWriter(socket.getOutputStream());
        Thread thread = new Thread() {
            @Override
            public void run() {
            }
        };
    }

    public char[] receive() throws IOException {
        int len;
        char chars[] = new char[64];
        if((len = reader.read(chars)) != -1) {
            buffer.append(new String(chars,0,len));
        }
        char[] data = buffer.toString().toCharArray();
        // 并发问题暂时无视
        buffer.setLength(0);
        return data;

    }

    public void send(char[] data) {
        try {
            writer.write("test");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

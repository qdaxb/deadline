package com.weibo.hackathon.deadline.engine.net;

import java.io.*;
import java.net.Socket;

/**
 * Created by axb on 15/4/17.
 */
public class NetworkChannel {
    private int i=0;
    private Socket socket;
    private Reader reader;
    private Writer writer;
    private StringBuffer buffer = new StringBuffer();
    public NetworkChannel(Socket socket) throws IOException {
        this.socket = socket;
        reader = new InputStreamReader(socket.getInputStream());
        writer = new OutputStreamWriter(socket.getOutputStream(),"ISO-8859-1");
        writer.write((char)255);
        writer.write((char)253);
        writer.write((char)34);
        writer.write((char)1);
        writer.write((char)0x0);
        writer.write((char)255);
        writer.write((char)240);
        writer.write((char)255);
        writer.write((char)251);
        writer.write((char)1);
        writer.write("\u001B[2J");
        writer.write((char)0x1b);
        writer.write((char)0x5b);
        writer.write((char)0x48);
        writer.flush();
        
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
            writer.write("**********************************\r\n");
            writer.write("**********************************\r\n");
            writer.write("**********************************\r\n");
            writer.write("**********************************\r\n");
            writer.write("**********************************\r\n");
            writer.write("**********************************\r\n");
            writer.write("**********************************\r\n");
            writer.write("**********************************\r\n");
            writer.write("**********************************\r\n");
            writer.write(i+++"**********************************\r\n");
            writer.write("**********************************\r\n");
            writer.write((char)0x1b);
            writer.write((char)0x5b);
            writer.write((char)0x48);

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

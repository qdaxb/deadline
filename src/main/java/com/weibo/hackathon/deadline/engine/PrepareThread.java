package com.weibo.hackathon.deadline.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.weibo.hackathon.deadline.engine.input.NetworkInput;
import com.weibo.hackathon.deadline.engine.input.RoomManager;
import com.weibo.hackathon.deadline.engine.net.NetworkChannel;
import com.weibo.hackathon.deadline.engine.output.NetworkOutputDevice;
import com.weibo.hackathon.deadline.engine.utils.GameSessionFactory;

public class PrepareThread extends Thread {
    private GameEngine engine;
    private NetworkChannel channel;

    public PrepareThread(GameEngine engine, NetworkChannel channel) {
        this.engine = engine;
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            ConcurrentHashMap<String, List<GameSession>> sessions = engine.getSessions();
            List<GameSession> sessionRoom;

            channel.sendRaw("Please enter your name! \r\n".toCharArray());
            String name = new String(channel.blockingReceive());
            name = name.replaceAll("\r\n", "");

            channel.sendRaw("enter 1 to play, or input roomname to have fun with other players \r\n".toCharArray());
            String command = new String(channel.blockingReceive());
            channel.init();
            if (command.equals("1\r\n")) {
                GameSession player = GameSessionFactory.createSession(new NetworkInput(channel), new NetworkOutputDevice(channel), name);
                sessionRoom = new ArrayList<GameSession>();
                sessionRoom.add(player);
                new RoomManager(sessionRoom).start();
            } else {
                String room = command;
                if (sessions.get(room) == null) {
                    sessionRoom = new ArrayList<GameSession>();
                    sessions.put(command, sessionRoom);
                    channel.sendRaw("Wait for other players! \r\n".toCharArray());
                }

                GameSession player = GameSessionFactory.createSession(new NetworkInput(channel), new NetworkOutputDevice(channel), name);
                sessionRoom = sessions.get(room);
                sessionRoom.add(player);
                if (sessionRoom.size() == 2) {
                    new RoomManager(sessionRoom).start();
                    sessions.remove(room);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

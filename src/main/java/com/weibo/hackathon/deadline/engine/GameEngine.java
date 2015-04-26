package com.weibo.hackathon.deadline.engine;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.weibo.hackathon.deadline.engine.input.InputManager;
import com.weibo.hackathon.deadline.engine.net.NetworkChannel;
import com.weibo.hackathon.deadline.engine.net.NetworkManager;
import com.weibo.hackathon.deadline.engine.output.OutputDevice;
import com.weibo.hackathon.deadline.engine.render.Render;

/**
 * 游戏引擎入口
 */
public class GameEngine {


    private InputManager inputManager;
    private ConcurrentHashMap<String, List<GameSession>> sessions = new ConcurrentHashMap<String, List<GameSession>>();
    private Render<String> render;
    private OutputDevice<String> outputDevice;

    public void start() {
     
    }

    public ConcurrentHashMap<String, List<GameSession>> getSessions() {
        return sessions;
    }

    public void setSessions(ConcurrentHashMap<String, List<GameSession>> sessions) {
        this.sessions = sessions;
    }

    public static void main(String[] args) throws IOException {
        // init input manager
        // init render
        // init gamecontroller
        GameEngine engine = new GameEngine();
        Properties properties = new Properties();
        NetworkManager manager = new NetworkManager();
        if (args.length >0) {
            manager.open(Integer.parseInt(args[0]));
        } else {
            manager.open(6666);
        }
        while (true) {
            NetworkChannel channel = manager.nextChannel();
            new PrepareThread(engine, channel).start();;
        }

    }

}

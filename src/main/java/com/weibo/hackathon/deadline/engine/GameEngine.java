package com.weibo.hackathon.deadline.engine;

import com.weibo.hackathon.deadline.engine.input.InputManager;
import com.weibo.hackathon.deadline.engine.input.NetworkInput;
import com.weibo.hackathon.deadline.engine.net.NetworkChannel;
import com.weibo.hackathon.deadline.engine.net.NetworkManager;
import com.weibo.hackathon.deadline.engine.output.NetworkOutputDevice;
import com.weibo.hackathon.deadline.engine.output.OutputDevice;
import com.weibo.hackathon.deadline.engine.render.Render;
import com.weibo.hackathon.deadline.engine.render.TextRender;
import com.weibo.hackathon.deadline.engine.utils.GameSessionFactory;

import java.io.IOException;
import java.util.*;

/**
 * 游戏引擎入口
 */
public class GameEngine {


    private GameController gameController;
    private InputManager inputManager;
    private List<GameSession> sessions = new ArrayList<GameSession>();
    private Render<String> render;
    private OutputDevice<String> outputDevice;

    public void start() {
        while (true) {
            try {
                Thread.sleep(100);
                for(int i=0;i<sessions.size();i++) {
                    sessions.get(i).getInputManager().getInputStatus();
                }
                String data = "BBBB";
                for(int i=0;i<sessions.size();i++) {
                    sessions.get(i).getOutputDevice().output(data);
                }
            } catch (InterruptedException e) {}
        }

    }

    public static void main(String[] args) throws IOException {
        // init input manager
        // init render
        // init gamecontroller
        GameEngine engine = new GameEngine();
        Properties properties = new Properties();
        NetworkManager manager = new NetworkManager();
        manager.open(8880);
        NetworkChannel channel = manager.nextChannel();

        GameSession player1 = GameSessionFactory.createSession(new NetworkInput(channel), new NetworkOutputDevice(channel));

        engine.sessions.add(player1);
        engine.start();


    }

}

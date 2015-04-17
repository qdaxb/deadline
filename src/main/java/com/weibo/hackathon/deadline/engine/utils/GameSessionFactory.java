package com.weibo.hackathon.deadline.engine.utils;

import com.weibo.hackathon.deadline.engine.GameSession;
import com.weibo.hackathon.deadline.engine.input.InputManager;
import com.weibo.hackathon.deadline.engine.output.OutputDevice;

/**
 * Created by axb on 15/4/17.
 */
public class GameSessionFactory {
    public static GameSession createSession(InputManager manager,OutputDevice device) {
        GameSession session = new GameSession();
        session.setInputManager(manager);
        session.setOutputDevice(device);
        return session;
    }
}

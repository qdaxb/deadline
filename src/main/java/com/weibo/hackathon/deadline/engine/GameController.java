package com.weibo.hackathon.deadline.engine;

import com.weibo.hackathon.deadline.controller.GameScene;
import com.weibo.hackathon.deadline.controller.GameSceneImpl;
import com.weibo.hackathon.deadline.engine.input.GameInput;
import com.weibo.hackathon.deadline.engine.model.Scene;

public interface GameController {
    

    public void input(GameInput input);
    
    public void setGameEngine(GameEngine engine);
    
    public Scene getScene();

    public abstract void resume();

    public abstract void pause();

    void init(String name);

    public abstract void stop();

    public abstract void setPlayers(int players);

    public abstract int getPlayers();

    public abstract void oneframe();

    public abstract void connect(GameController gc);
    
}

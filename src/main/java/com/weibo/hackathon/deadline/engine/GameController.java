package com.weibo.hackathon.deadline.engine;

import com.weibo.hackathon.deadline.engine.input.GameInput;
import com.weibo.hackathon.deadline.engine.model.Scene;

public interface GameController {
    
    public void init();
    
    public void input(GameInput input);
    
    public void setGameEngine(GameEngine engine);
    
    public Scene getScene();

    public abstract void resume();

    public abstract void pause();

    public abstract void stop();
    
}

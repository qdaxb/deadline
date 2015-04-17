package com.weibo.hackathon.deadline.engine.flow;

import com.weibo.hackathon.deadline.engine.GameEngine;
import com.weibo.hackathon.deadline.engine.input.GameInput;

public interface GameController {
    
    public void init();
    
    public void input(GameInput input);
    
    public void setGameEngine(GameEngine engine);
    
}

package com.weibo.hackathon.deadline.controller;

import com.weibo.hackathon.deadline.engine.Action;

public interface ActionGenerator {
    
    Action nextAction();
    
    boolean isOver();
    
    boolean isNextAvailable();

}

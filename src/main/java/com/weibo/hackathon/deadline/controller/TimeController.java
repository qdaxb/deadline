package com.weibo.hackathon.deadline.controller;

public interface TimeController {
    
    public void pause();
    public void resume();
    public int timeInterval();
    public boolean isPaused();

}

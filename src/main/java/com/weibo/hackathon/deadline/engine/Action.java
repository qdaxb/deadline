package com.weibo.hackathon.deadline.engine;

public interface Action {
    
    public void perform(int frames);
    
    public boolean active();

}

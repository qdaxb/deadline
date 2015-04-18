package com.weibo.hackathon.deadline.controller;

import com.weibo.hackathon.deadline.engine.input.GameInput;
import com.weibo.hackathon.deadline.engine.model.Scene;



public interface GameScene {

    public abstract void cancel();

    public abstract void fail();

    public abstract Scene getScene();

    public abstract void playerInput(GameInput input);

    public abstract boolean isOver();

    public abstract void success();

}

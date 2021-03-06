package com.weibo.hackathon.deadline.engine;

import com.weibo.hackathon.deadline.engine.input.InputManager;
import com.weibo.hackathon.deadline.engine.output.OutputDevice;

/**
 * Created by axb on 15/4/17.
 */
public class GameSession {
    private InputManager inputManager;
    private OutputDevice outputDevice;
    private String playerName;
    
    public void close(){
        
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public OutputDevice getOutputDevice() {
        return outputDevice;
    }

    public void setOutputDevice(OutputDevice outputDevice) {
        this.outputDevice = outputDevice;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}

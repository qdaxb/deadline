package com.weibo.hackathon.deadline.controller;

import java.util.HashMap;
import java.util.Map;

import com.weibo.hackathon.deadline.controller.action.MoveAction;
import com.weibo.hackathon.deadline.engine.GameController;
import com.weibo.hackathon.deadline.engine.GameEngine;
import com.weibo.hackathon.deadline.engine.input.GameInput;
import com.weibo.hackathon.deadline.engine.model.Scene;

public class GameControllerImpl implements Runnable, GameController {

    private static final long REFRESH_INTERVAL = 100;

    private static final Map<GameInput, Integer> STRAIGHT_SPEED = new HashMap<GameInput, Integer>();

    static {
        STRAIGHT_SPEED.put(GameInput.UP, -1);
        STRAIGHT_SPEED.put(GameInput.DOWN, 1);
    }

    Thread worker;

    ActionGenerator actionGenerator;

    GameScene gameScene;

    private GameEngine engine;

    TimeController timeController;

    @Override
    public synchronized void init() {
        if (worker == null) {
            worker = new Thread(this);
            worker.start();
        }
    }

    private void postpare() {
        // TODO Auto-generated method stub

    }

    private void prepare() {
        gameScene = new GameScene();
        actionGenerator = new RandomActionGenerator();
    }

    @Override
    public void stop() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    private boolean isOver() {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean isPaused() {
        return timeController.isPaused();
    }

    public long timeEllapse() {
        return timeController.timeInterval();
    }

    public void mainLoop() {
        while (!isOver()) {
            if (!isPaused()) {
                long duration = timeEllapse();
                if (duration > 0) {
                    gameScene.oneStep(duration);
                }
            }
            try {
                Thread.sleep(REFRESH_INTERVAL);
            } catch (InterruptedException e) {}
        }
    }

    @Override
    public void input(GameInput input) {
        MoveAction act = new MoveAction();
        // act.element = gameScene.player();
        act.paralell = STRAIGHT_SPEED.get(input);
        if (input == GameInput.UP) {}
    }

    @Override
    public Scene getScene() {
        return gameScene.getScene();
    }

    @Override
    public void setGameEngine(GameEngine engine) {
        this.engine = engine;
    }

    @Override
    public void run() {
        prepare();
        mainLoop();
        postpare();
    }

    public GameEngine getEngine() {
        return engine;
    }

}

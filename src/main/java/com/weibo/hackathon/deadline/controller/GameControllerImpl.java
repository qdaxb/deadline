package com.weibo.hackathon.deadline.controller;

import java.util.HashMap;
import java.util.Map;

import com.weibo.hackathon.deadline.engine.GameController;
import com.weibo.hackathon.deadline.engine.GameEngine;
import com.weibo.hackathon.deadline.engine.input.GameInput;
import com.weibo.hackathon.deadline.engine.model.Location;
import com.weibo.hackathon.deadline.engine.model.Player;
import com.weibo.hackathon.deadline.engine.model.Scene;
import com.weibo.hackathon.deadline.engine.model.Size;

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
            prepare();
            // worker.start();
            // postpare();
        }
    }

    // private void postpare() {}

    private void prepare() {
        gameScene = new GameScene();
        actionGenerator = new RandomActionGenerator(gameScene);
        timeController = new Frame100TimeController();
        gameScene.actionGenerator = actionGenerator;

        Player player = new Player();
        Size sceneSize = gameScene.scene.size;
        player.loc = new Location(sceneSize.width / 2, sceneSize.height / 2);
        player.size = new Size(5, 3);
        gameScene.setPlayer(player);
    }

    @Override
    public void stop() {
        gameScene.cancel();
    }

    @Override
    public void pause() {
        timeController.pause();
    }

    @Override
    public void resume() {
        timeController.resume();
    }

    private boolean isOver() {
        return gameScene.isOver();
    }

    private boolean isPaused() {
        return timeController.isPaused();
    }

    public int timeEllapse() {
        return timeController.timeInterval();
    }

    public void mainLoop() {
        while (!isOver()) {
            if (!isPaused()) {
                int duration = timeEllapse();
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
        gameScene.playerInput(input);
        gameScene.oneStep(1);
    }

    public void oneframe() {
        gameScene.oneStep(1);
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
        mainLoop();
    }

    public GameEngine getEngine() {
        return engine;
    }

}

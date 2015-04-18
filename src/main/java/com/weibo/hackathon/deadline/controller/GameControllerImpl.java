package com.weibo.hackathon.deadline.controller;

import java.util.HashMap;
import java.util.Map;

import com.weibo.hackathon.deadline.controller.GameSceneImpl.Switcher;
import com.weibo.hackathon.deadline.controller.action.PipeActionGenerator;
import com.weibo.hackathon.deadline.engine.GameController;
import com.weibo.hackathon.deadline.engine.GameEngine;
import com.weibo.hackathon.deadline.engine.input.GameInput;
import com.weibo.hackathon.deadline.engine.model.Location;
import com.weibo.hackathon.deadline.engine.model.Player;
import com.weibo.hackathon.deadline.engine.model.Scene;
import com.weibo.hackathon.deadline.engine.model.Size;

public class GameControllerImpl implements GameController {

    private static final Map<GameInput, Integer> STRAIGHT_SPEED = new HashMap<GameInput, Integer>();

    static {
        STRAIGHT_SPEED.put(GameInput.UP, -1);
        STRAIGHT_SPEED.put(GameInput.DOWN, 1);
    }

    Thread worker;

    AggregateActionGenerator actionGenerator;

    GameSceneImpl gameScene;

    private GameEngine engine;

    private boolean prepared = false;

    public Switcher sw = new Switcher();

    @Override
    public synchronized void init(String name) {
        if (!prepared) {
            prepare1(name);
            prepared = true;
        }
    }

    private int players = 1;

    private GameControllerImpl peer;

    private void prepare1(String name) {
        gameScene = new GameSceneImpl(name);
        actionGenerator = new AggregateActionGenerator();
        actionGenerator.generators.add(new RandomActionGenerator(gameScene));
        gameScene.actionGenerator = actionGenerator;

        Player player = new Player();
        Size sceneSize = gameScene.scene.size;
        player.loc = new Location(sceneSize.width / 2, sceneSize.height / 2);
        player.size = new Size(3, 3);
        gameScene.setPlayer(player);
        gameScene.setSwitcher(sw);
    }

    @Override
    public void stop() {
        gameScene.cancel();
    }

    @Override
    public void pause() {
        sw.turnOff();
    }

    @Override
    public void resume() {
        sw.turnOn();
    }

    @Override
    public void input(GameInput input) {
        gameScene.playerInput(input);
        gameScene.oneStep();
    }

    @Override
    public void oneframe() {
        gameScene.oneStep();
        if (peer != null) {
            peer.gameScene.oneStep();
        }
    }

    @Override
    public void connect(GameController gc) {
        if (gc instanceof GameControllerImpl) {
            GameControllerImpl gci = (GameControllerImpl) gc;
            gci.gameScene.setSwitcher(sw);

            PipeActionGenerator pag1, pag2;
            pag1 = new PipeActionGenerator();
            pag2 = new PipeActionGenerator();
            pag1.pipeWith(pag2);
            
            //actionGenerator.generators.add(0, pag1);
            //actionGenerator.generators.add(0, pag2);

            gameScene.eventActionGenerator = pag1;
            gci.gameScene.eventActionGenerator = pag2;

            pag1.gs = gameScene;
            pag2.gs = gci.gameScene;

            gameScene.pipe = pag1;
            gci.gameScene.pipe = pag2;

            peer = gci;
            gci.peer = this;
        }
    }

    @Override
    public Scene getScene() {
        return gameScene.getScene();
    }

    @Override
    public void setGameEngine(GameEngine engine) {
        this.engine = engine;
    }

    public GameEngine getEngine() {
        return engine;
    }

    @Override
    public int getPlayers() {
        return players;
    }

    @Override
    public void setPlayers(int players) {
        this.players = players;
    }

}

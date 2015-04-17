package com.weibo.hackathon.deadline.controller;

import java.util.Iterator;
import java.util.List;

import com.weibo.hackathon.deadline.engine.Action;
import com.weibo.hackathon.deadline.engine.GameResult;
import com.weibo.hackathon.deadline.engine.input.GameInput;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.Scene;

public class GameScene {

    Scene scene;
    List<Property> objects;
    Property player;
    GameResult result = null;

    public Action getAction(Element elem, Class<? extends Action> c) {
        return null;
    }

    public void apply(Element elem, Action act) {

    }

    public void oneStep(int duration) {
        Iterator<Property> it = objects.iterator();
        while (it.hasNext()) {
            Property act = it.next();
            act.xMove.perform(duration);
            act.yMove.perform(duration);
        }
        determine();
    }

    private void determine() {
        Point point = player.getPoint();
        if (point.x < 1) {
            fail();
        } else if (point.x + player().size.width >= scene.size.width) {
            point.x = scene.size.width - player().size.width - 1;
            player.setPoint(point);
        }

        if (point.y < 1) {
            point.y = 1;
            player.setPoint(point);
        } else if (point.y + player().size.height >= scene.size.height) {
            point.x = scene.size.height - player().size.height - 1;
            player.setPoint(point);
        }
    }

    private void fail() {
        result = GameResult.FAIL;
    }

    public Element player() {
        return player.element;
    }

    public Scene getScene() {
        return scene;
    }

    public void playerInput(GameInput input) {
        if (input == GameInput.UP) {
            player.yMove.setDirection(1);
        } else {
            player.yMove.setDirection(-1);
        }
        player.yMove.steps = 1;
    }

}

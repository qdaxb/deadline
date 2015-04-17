package com.weibo.hackathon.deadline.controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.weibo.hackathon.deadline.controller.action.MoveAction;
import com.weibo.hackathon.deadline.engine.Action;
import com.weibo.hackathon.deadline.engine.GameResult;
import com.weibo.hackathon.deadline.engine.input.GameInput;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.Location;
import com.weibo.hackathon.deadline.engine.model.Player;
import com.weibo.hackathon.deadline.engine.model.Scene;

public class GameScene {

    private static final int FORWARD = 1;
    private static final int BACKWARD = -1;
    private static final int X_PIXAL_TU_COST = 50;
    Scene scene = new Scene();
    private final List<Property> objects = new LinkedList<Property>();
    Property player;
    GameResult result = null;
    ActionGenerator actionGenerator;

    public Action getAction(Element elem, Class<? extends Action> c) {
        return null;
    }

    public void apply(Element elem, Action act) {

    }

    public void cancel() {
        result = GameResult.CANCELLED;
    }

    public void oneStep(int duration) {
        Iterator<Property> it = objects.iterator();
        while (it.hasNext()) {
            Property act = it.next();
            act.xMove.perform();
            act.yMove.perform();
        }
        while (actionGenerator.isNextAvailable()) {
            Action action = actionGenerator.nextAction();
            action.perform(1);
        }
        determine();
    }

    private void determine() {
        synchronized (objects) {
            if (objects.size() == 1 && actionGenerator.isOver()) {
                result = GameResult.SUCCESS;
            } else {
                Iterator<Property> it = objects.iterator();
                while (it.hasNext()) {
                    Property prop = it.next();
                    if (prop.element instanceof Player) {
                        Point point = prop.getPoint();
                        if (point.x < 1) {
                            fail();
                        } else if (point.x + prop.element.size.width >= scene.size.width) {
                            point.x = scene.size.width - prop.element.size.width - 1;
                            prop.setPoint(point);
                        }

                        if (point.y < 1) {
                            point.y = 1;
                            prop.setPoint(point);
                        } else if (point.y + prop.element.size.height >= scene.size.height) {
                            point.y = scene.size.height - prop.element.size.height - 1;
                            prop.setPoint(point);
                        }
                    } else {
                        Point point = prop.getPoint();
                        if (point.x < 1) {
                            prop.disappear = true;
                        } else if (point.x + prop.element.size.width >= scene.size.width) {
                            prop.disappear = true;
                        }

                        if (point.y < 1 - prop.element.size.height) {
                            prop.disappear = true;
                        } else if (point.y >= scene.size.height - 1) {
                            prop.disappear = true;
                        }
                        if (prop.disappear) {
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    private void fail() {
        result = GameResult.FAIL;
    }

    public Element player() {
        return player.element;
    }

    public Scene getScene() {
        scene.elements.clear();
        synchronized (objects) {
            Iterator<Property> it = objects.iterator();
            while (it.hasNext()) {
                Property prop = it.next();
                prop.getPoint();
                if (prop.disappear) {
                    it.remove();
                } else {
                    scene.elements.add(prop.element);
                }
            }
        }
        return scene;
    }

    public void playerInput(GameInput input) {
        if (input == GameInput.UP) {
            player.yMove.setDirection(FORWARD);
        } else if (input == GameInput.DOWN) {
            player.yMove.setDirection(BACKWARD);
        } else {
            return;
        }
        player.yMove.steps = 1;
    }

    public boolean isOver() {
        return result != null;
    }

    public void setPlayer(Player player) {
        if (player == null) {
            return;
        } else {
            Location loc = player.loc;
            Property prop = new Property();
            prop.element = player;
            prop.disappear = false;
            prop.setPoint(new Point(loc.height, loc.width));

            // prop.xMove = new MoveAction();
            // prop.xMove.setDirection(0);
            // prop.xMove.setSpeed(X_PIXAL_TU_COST);
            // prop.xMove.setSteps(Integer.MAX_VALUE); // infinite
            //
            // prop.yMove = new MoveAction();
            // prop.yMove.setSteps(0);

            objects.add(prop);
            this.player = prop;
        }

    }

    public void addElement(Element element) {
        if (element == null) {
            return;
        } else {
            Location loc = element.loc;
            Property prop = new Property();
            prop.element = element;
            prop.disappear = false;
            prop.setPoint(new Point(loc.width, loc.height));

            prop.xMove.setDirection(BACKWARD);
            prop.xMove.setSteps(Integer.MAX_VALUE); // infinite
            prop.xMove.setSpeed(5);

            prop.yMove.setSteps(0);

            objects.add(prop);
        }

    }

}

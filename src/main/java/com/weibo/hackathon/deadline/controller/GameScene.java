package com.weibo.hackathon.deadline.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.weibo.hackathon.deadline.controller.action.MoveAction;
import com.weibo.hackathon.deadline.engine.Action;
import com.weibo.hackathon.deadline.engine.GameResult;
import com.weibo.hackathon.deadline.engine.input.GameInput;
import com.weibo.hackathon.deadline.engine.model.Block;
import com.weibo.hackathon.deadline.engine.model.Candy;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.Location;
import com.weibo.hackathon.deadline.engine.model.Player;
import com.weibo.hackathon.deadline.engine.model.Scene;

public class GameScene {

    public static final org.slf4j.Logger log = LoggerFactory.getLogger(GameScene.class);

    private static final float OBJECT_SPEED = -0.2f;
    private static final float X_SPEED = 0.5f;
    private static final int FORWARD = 1;
    private static final int BACKWARD = -1;

    Scene scene = new Scene();
    private final List<Property> objects = new LinkedList<Property>();
    Property player;
    GameResult result = null;
    ActionGenerator actionGenerator;
    private static final Comparator<Property> xComparator = new Comparator<Property>() {

        @Override
        public int compare(Property o1, Property o2) {
            return Integer.compare(o1.getPoint().x, o2.getPoint().x);
        }
    };

    private static final int TTL = 10;

    public void cancel() {
        result = GameResult.CANCELLED;
    }

    public void oneStep() {
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
        dealWithBlock();
        dealWithCandy();
    }

    private void dealWithCandy() {
        List<Property> list = new ArrayList<Property>();
        Iterator<Property> it = objects.iterator();
        while (it.hasNext()) {
            Property elem = it.next();
            if ((elem.element instanceof Candy || elem == player) && onWay(elem)) {
                list.add(elem);
            }
        }
        Collections.sort(list, xComparator);
        if (list.size() <= 1) {
            return;
        } else {
            if (siblingCount(list) > 1) {
                objects.remove(list.get(1));
                player.xMove.setSpeed(X_SPEED);
                player.xMove.steps = 10;
                System.out.printf("meet candies: speed change to %s seconds.\r\n", X_SPEED);
            }
        }
    }

    private void dealWithBlock() {
        List<Property> list = new ArrayList<Property>();
        Iterator<Property> it = objects.iterator();
        while (it.hasNext()) {
            Property elem = it.next();
            if ((elem.element instanceof Block || elem == player) && onWay(elem)) {
                list.add(elem);
            }
        }
        if (list.size() > 1) {
            Collections.sort(list, xComparator);

            int n = siblingCount(list);
            if (n <= 1) {
                recover();
                return;
            }

            float v = 0;
            for (int i = 0; i < n; i++) {
                MoveAction xMove = list.get(i).xMove;
                v += xMove.speed;
            }
            float x = v / n;
            System.out.printf("meet %s blocks: speed change to %s seconds.\r\n", n, x);
            for (int i = 0; i < n; i++) {
                Property property = list.get(i);
                property.xMove.setSpeed(x);
                if (player == property) {
                    property.setTTL(Integer.MAX_VALUE);
                } else if (property.getTTL() > TTL) {
                    property.setTTL(TTL);
                }
            }
        } else {
            recover();
        }
    }

    private void recover() {
        player.setSpeed(0f);
    }

    private int siblingCount(List<Property> list) {
        int c = 1;
        for (int i = 0; i < list.size() - 1; i++) {
            Property p1 = list.get(i);
            Property p2 = list.get(i + 1);
            if (p1.getPoint().x + p1.element.size.width == p2.getPoint().x) {
                c++;
            } else {
                break;
            }
        }
        return c;
    }

    private boolean onWay(Property p) {
        if (p == player) {
            return true;
        }
        Point pp = player.getPoint();
        Point pe = p.getPoint();
        boolean elementNotTooLow = pp.y < pe.y + p.element.size.height;
        boolean elementNotTooHigh = pp.y + player.element.size.height > pe.y;
        return pp.x <= pe.x && elementNotTooLow && elementNotTooHigh;
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
                            prop.setDisappear();
                        } else if (point.x + prop.element.size.width >= scene.size.width) {
                            prop.setDisappear();
                        }

                        if (point.y < 1 - prop.element.size.height) {
                            prop.setDisappear();
                        } else if (point.y >= scene.size.height - 1) {
                            prop.setDisappear();
                        }
                        if (prop.shouldDisappear()) {
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
                if (prop.shouldDisappear()) {
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
            player.yMove.setSpeed(FORWARD);
        } else if (input == GameInput.DOWN) {
            player.yMove.setSpeed(BACKWARD);
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
            prop.setPoint(new Point(loc.height, loc.width));
            prop.setTTL(Integer.MAX_VALUE);

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
            prop.setPoint(new Point(loc.width, loc.height));

            prop.xMove.setSteps(Integer.MAX_VALUE);
            prop.xMove.setSpeed(OBJECT_SPEED);

            prop.yMove.setSteps(0);

            objects.add(prop);
        }

    }

}

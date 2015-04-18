package com.weibo.hackathon.deadline.controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.weibo.hackathon.deadline.controller.action.PipeActionGenerator;
import com.weibo.hackathon.deadline.engine.Action;
import com.weibo.hackathon.deadline.engine.GameResult;
import com.weibo.hackathon.deadline.engine.input.GameInput;
import com.weibo.hackathon.deadline.engine.model.Block;
import com.weibo.hackathon.deadline.engine.model.Candy;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.InfoElement;
import com.weibo.hackathon.deadline.engine.model.Location;
import com.weibo.hackathon.deadline.engine.model.Player;
import com.weibo.hackathon.deadline.engine.model.Scene;
import com.weibo.hackathon.deadline.engine.model.TrickyCandy;

public class GameSceneImpl implements GameScene {

    public static class Switcher {

        private boolean on = true;

        public boolean isOn() {
            return on;
        }

        public void turnOn() {
            this.on = true;
        };

        public void turnOff() {
            this.on = false;
        }
    }

    private static class RateControl {
        private int l;
        private int c = 0;
        private Switcher switcher;

        public RateControl(int l) {
            this.l = l;
        }

        public void setSwitcher(Switcher sw) {
            this.switcher = sw;
        }

        public boolean fulfill() {
            // pause;
            if (switcher == null || !switcher.isOn()) {
                return false;
            }
            c++;
            if (c >= l) {
                c = 0;
                return true;
            } else {
                return false;
            }
        }
    }

    public void setSwitcher(Switcher sw) {
        for (RateControl rc : Arrays.asList(H, V, G)) {
            rc.setSwitcher(sw);
        }
    }

    private static final int FORWARD = 1;
    private static final int STOP = 0;
    private static final int BACKWARD = -1;
    private final RateControl H = new RateControl(10), V = new RateControl(2), G = new RateControl(50);

    private static final int TTL = 10;
    private static final int CANDY_BONOUS = 5;

    Scene scene = new Scene();
    private final List<Property> objects = new LinkedList<Property>();
    Property player;
    GameResult result = null;
    ActionGenerator actionGenerator;

    /*
     * (non-Javadoc)
     * 
     * @see com.weibo.hackathon.deadline.controller.GS#cancel()
     */
    @Override
    public void cancel() {
        pipe.reportResult(GameResult.CANCELLED);
    }

    public void oneStep() {
        if (result == null) {
            if (H.fulfill()) {
                Iterator<Property> it = objects.iterator();
                while (it.hasNext()) {
                    Property act = it.next();
                    act.xMove.perform();
                }
                player.xMove.perform();
                if (xForward > 0) {
                    player.xMove.perform();
                    xForward--;
                }
            }
            if (V.fulfill()) {
                Iterator<Property> it = objects.iterator();
                while (it.hasNext()) {
                    Property act = it.next();
                    act.yMove.perform();
                }
                player.yMove.perform();
            }
            if (G.fulfill() && actionGenerator.isNextAvailable()) {
                Action action = actionGenerator.nextAction();
                action.perform();
            }
            deal();
            determine();
        }
    }

    private Property findAdjacentObject() {
        Iterator<Property> it = objects.iterator();
        while (it.hasNext()) {
            Property elem = it.next();
            Property p = elem;
            if (p == player) {
                continue;
            } else {
                Point piontPlayer = player.getPoint();
                Point pointObject = p.getPoint();
                boolean elementNotTooLow = piontPlayer.y < pointObject.y + p.element.size.height;
                boolean elementNotTooHigh = piontPlayer.y + player.element.size.height > pointObject.y;
                if (piontPlayer.x <= pointObject.x && piontPlayer.x + player.element.size.width >= pointObject.x && elementNotTooLow
                        && elementNotTooHigh) {
                    return (elem);
                }
            }
        }
        return null;
    }

    private int xForward = 0;
    public PipeActionGenerator pipe;

    private void deal() {
        Property adj = findAdjacentObject();
        if (adj == null) {
            if (xForward <= 0) {
                player.xMove.setMovement(STOP);
            }
            return;
        } else if (adj.element instanceof Block) {
            player.xMove.setMovement(BACKWARD);
            adj.setTTL(TTL);
        } else if (adj.element instanceof Candy) {
            player.xMove.setMovement(FORWARD);
            adj.setTTL(0);
            xForward = CANDY_BONOUS;

            if (adj.element instanceof TrickyCandy) {
                if (pipe != null) {
                    pipe.makeBlock();
                }
            }
        }
    }

    private void determine() {
        synchronized (objects) {
            if (objects.size() == 1 && actionGenerator.isOver()) {
                result = GameResult.SUCCESS;
            } else {
                Iterator<Property> it = objects.iterator();
                while (it.hasNext()) {
                    Property prop = it.next();
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
                Point point = player.getPoint();
                if (point.x < 1) {
                    fail();
                } else if (point.x + player.element.size.width >= scene.size.width) {
                    point.x = scene.size.width - player.element.size.width - 1;
                    player.setPoint(point);
                }

                if (point.y < 1) {
                    point.y = 1;
                    player.setPoint(point);
                } else if (point.y + player.element.size.height >= scene.size.height) {
                    point.y = scene.size.height - player.element.size.height - 1;
                    player.setPoint(point);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.weibo.hackathon.deadline.controller.GS#fail()
     */
    @Override
    public void fail() {
        makeResult(GameResult.FAIL);
    }

    private void makeResult(GameResult r) {
        result = r;
        if(pipe != null) {
            pipe.reportResult(result);
        }
    }

    public Element player() {
        return player.element;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.weibo.hackathon.deadline.controller.GS#getScene()
     */
    @Override
    public Scene getScene() {
        scene.elements.clear();
        if (!isOver()) {
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
                player.getPoint();
                scene.elements.add(player.element);
            }
        } else {
            scene.elements.add(new InfoElement(result));
        }
        return scene;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.weibo.hackathon.deadline.controller.GS#playerInput(com.weibo.hackathon.deadline.engine
     * .input.GameInput)
     */
    @Override
    public void playerInput(GameInput input) {
        if (input == GameInput.UP) {
            player.setVerticalMovement(FORWARD);
        } else if (input == GameInput.DOWN) {
            player.setVerticalMovement(BACKWARD);
        } else {
            return;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.weibo.hackathon.deadline.controller.GS#isOver()
     */
    @Override
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
            prop.xMove.setMovement(STOP);
            prop.setTTL(Integer.MAX_VALUE);
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
            prop.xMove.setMovement(BACKWARD);

            prop.yMove.setSteps(0);

            objects.add(prop);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.weibo.hackathon.deadline.controller.GS#success()
     */
    @Override
    public void success() {
        makeResult(GameResult.SUCCESS);
    }

}

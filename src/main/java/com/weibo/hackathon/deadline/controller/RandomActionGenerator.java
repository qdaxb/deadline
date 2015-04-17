package com.weibo.hackathon.deadline.controller;

import java.util.Random;

import com.weibo.hackathon.deadline.controller.action.MakeObjectAction;
import com.weibo.hackathon.deadline.engine.Action;
import com.weibo.hackathon.deadline.engine.model.Block;
import com.weibo.hackathon.deadline.engine.model.Candy;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.Location;
import com.weibo.hackathon.deadline.engine.model.Size;

public class RandomActionGenerator implements ActionGenerator {

    GameScene gs;
    public static Random rnd = new Random();

    public RandomActionGenerator(GameScene gs) {
        super();
        this.gs = gs;
    }

    Action action = null;
    boolean over = false;

    @Override
    public Action nextAction() {
        Action act = action;
        action = null;
        return act;
    }

    @Override
    public boolean isOver() {
        return over;
    }

    @Override
    public boolean isNextAvailable() {
        if (action != null) {
            return true;
        } else {
            if (rnd.nextDouble() > 0.98) {
                over = true;
                return false;
            } else if (rnd.nextDouble() > 0.98) {
                Element elem = null;
                int type = rnd.nextInt(2);
                if (type == 0) {
                    elem = new Block();
                } else {
                    elem = new Candy();
                }
                elem.size = new Size(rnd.nextInt(5) + 1, rnd.nextInt(5) + 1);
                Size size = gs.getScene().size;
                elem.loc = new Location(rnd.nextInt(size.height - elem.size.height - 2) + 1, size.width - elem.size.width - 1);
                MakeObjectAction act = new MakeObjectAction(elem, gs);
                action = act;
            }
            return action != null;
        }
    }

}

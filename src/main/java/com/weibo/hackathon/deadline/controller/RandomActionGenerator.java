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

    GameSceneImpl gs;
    public static Random rnd = new Random();

    public RandomActionGenerator(GameSceneImpl gs) {
        super();
        this.gs = gs;
    }

    public boolean randX = false;

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
            } else {
                Element elem = null;
                int type = rnd.nextInt(20);
                if (type != 0) {
                    elem = new Block();
                } else {
                    elem = new Candy();
                }
                elem.size = new Size(rnd.nextInt(5) + 1, rnd.nextInt(5) + 1);
                Size size = gs.getScene().size;
                int height = rnd.nextInt(size.height - elem.size.height - 2) + 1;
                int width = size.width - elem.size.width - 1;
                if (randX) {
                    width = rnd.nextInt(width - 1) + 1;
                }
                elem.loc = new Location(height, width);
                MakeObjectAction act = new MakeObjectAction(elem, gs);
                action = act;
                return action != null;
            }
        }
    }

}

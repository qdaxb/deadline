package com.weibo.hackathon.deadline.controller;

import java.util.Iterator;
import java.util.PriorityQueue;

import com.weibo.hackathon.deadline.engine.Action;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.Location;
import com.weibo.hackathon.deadline.engine.model.Scene;

public class GameScene {

    Scene scene;
    PriorityQueue<Property> motions;
    PriorityQueue<Action> actions;

    public Action getAction(Element elem, Class<? extends Action> c) {
        return null;
    }

    public void apply(Element elem, Action act) {

    }

    public Element getElementAt(Location loc) {
        return null;
    }

    public boolean isAdjacent(Element elem1, Element elem2) {
        return false;
    }

    public boolean onStartSide(Element elem, Element accordance) {
        return false;
    }

    public void oneStep(long duration) {
        Iterator<Action> it = actions.iterator();
        while (it.hasNext()) {
            Action act = it.next();
            act.perform();
            if (!act.active()) {
                it.remove();
            }
        }
        determine();
    }

    private void determine() {
        // TODO Auto-generated method stub

    }

    public Element player() {
        // TODO Auto-generated method stub
        return null;
    }

    public Scene getScene() {
        // TODO Auto-generated method stub
        return null;
    }

}

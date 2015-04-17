package com.weibo.hackathon.deadline.controller;

import com.weibo.hackathon.deadline.engine.Action;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.Location;
import com.weibo.hackathon.deadline.engine.model.Scene;

public class GameScene {

    Scene scene;

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

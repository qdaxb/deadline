package com.weibo.hackathon.deadline.controller.action;

import com.weibo.hackathon.deadline.controller.GameScene;
import com.weibo.hackathon.deadline.engine.Action;
import com.weibo.hackathon.deadline.engine.model.Element;

public class MakeObjectAction implements Action {

    Element element;
    GameScene gs;
    boolean active = true;

    public MakeObjectAction(Element element, GameScene gs) {
        super();
        this.element = element;
        this.gs = gs;
    }

    @Override
    public void perform(int frames) {
        gs.addElement(element);
        active = false;
    }

    @Override
    public boolean active() {
        return active;
    }

}

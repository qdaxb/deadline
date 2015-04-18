package com.weibo.hackathon.deadline.controller.action;

import com.weibo.hackathon.deadline.controller.GameSceneImpl;
import com.weibo.hackathon.deadline.engine.Action;
import com.weibo.hackathon.deadline.engine.model.Element;

public class MakeObjectAction implements Action {

    Element element;
    GameSceneImpl gs;

    public MakeObjectAction(Element element, GameSceneImpl gs) {
        super();
        this.element = element;
        this.gs = gs;
    }

    @Override
    public void perform() {
        gs.addElement(element);
    }


}

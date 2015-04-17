package com.weibo.hackathon.deadline.controller.action;

import com.weibo.hackathon.deadline.controller.Group;
import com.weibo.hackathon.deadline.controller.Momentum;
import com.weibo.hackathon.deadline.controller.Point;
import com.weibo.hackathon.deadline.controller.Property;
import com.weibo.hackathon.deadline.engine.Action;

public class MoveAction implements Action {

    public Property target;

    public int paralell;
    public int forward;
    public Momentum result;
    public Point point;
    public Group group;

    @Override
    public void action(int duration) {
        point = target.getPoint();
        point.x += duration * forward;
        point.y += duration * paralell;
    }

}

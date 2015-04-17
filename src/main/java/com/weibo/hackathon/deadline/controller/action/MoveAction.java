package com.weibo.hackathon.deadline.controller.action;

import com.weibo.hackathon.deadline.controller.Point;
import com.weibo.hackathon.deadline.controller.Property;
import com.weibo.hackathon.deadline.engine.Action;

public class MoveAction implements Action {

    public Property target;

    public int paralell;
    public int forward;
    public Point point;

    public int frame;
    public int xframe, yframe;
    public int xremain, yremain;
    public int xset, yset;

    @Override
    public void perform() {
        if (xremain > 0) {
            xremain--;
        } else {
            xremain = xset;
            xframe--;
            point.x += forward;
        }
        if (yremain > 0) {
            yremain--;
        } else {
            yremain = yset;
            yframe--;
            point.y += paralell;
        }
        target.setPoint(point);
    }

    @Override
    public boolean active() {
        return frame > 0;
    }

}

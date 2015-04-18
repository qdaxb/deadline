package com.weibo.hackathon.deadline.controller;

import com.weibo.hackathon.deadline.controller.action.MoveAction;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.Location;

public class Property {

    public Element element;

    public MoveAction xMove = new MoveAction(), yMove = new MoveAction();

    public int getTTL() {
        return xMove.steps;
    };

    public boolean shouldDisappear() {
        return xMove.steps <= 0;
    }

    public void setDisappear() {
        xMove.steps = 0;
    }

    public Point getPoint() {
        Point point = new Point(xMove.shift, yMove.shift);
        setPoint(point);
        return point;
    }

    public void setPoint(Point p) {
        element.loc = new Location(p.y, p.x);
        xMove.shift = p.x;
        yMove.shift = p.y;
    }

    public void setTTL(int ttl) {
        xMove.steps = ttl;
    }

    public void setHorizontalMovement(int mov) {
        xMove.setMovement(mov);
    }

    public void setVerticalMovement(int mov) {
        yMove.setMovement(mov);
        yMove.setSteps(1);
    }

}

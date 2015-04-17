package com.weibo.hackathon.deadline.controller;

import com.weibo.hackathon.deadline.controller.action.MoveAction;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.Location;

public class Property {

    public int weight;

    public int speed;

    public Element element;

    public boolean disappear;

    public MoveAction xMove = new MoveAction(), yMove = new MoveAction();

    public Point getPoint() {
        Point point = new Point(xMove.shift, yMove.shift);
        setPoint(point);
        return point;
    }

    public void setPoint(Point p) {
        element.loc = new Location(p.y, p.x);
        // if (xMove != null) {
        xMove.shift = p.x;
        // }
        // if (yMove != null) {
        yMove.shift = p.y;
        // }
    }

}

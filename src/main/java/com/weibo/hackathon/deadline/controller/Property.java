package com.weibo.hackathon.deadline.controller;

import com.weibo.hackathon.deadline.controller.action.MoveAction;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.Location;

public class Property {

    public int weight;

    public int speed;

    public Element element;

    public boolean disappear;

    public MoveAction xMove, yMove;

    public Point getPoint() {
        Point point = new Point(element.loc.width, element.loc.height);
        setPoint(point);
        return point;
    }

    public void setPoint(Point p) {
        element.loc = new Location(p.x, p.y);
    }

}

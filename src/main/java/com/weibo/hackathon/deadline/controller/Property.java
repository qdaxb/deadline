package com.weibo.hackathon.deadline.controller;

import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.Location;

public class Property {

    public int weight;

    public int speed;

    public Element element;
    
    public boolean disappear;

    public Point getPoint() {
        return new Point(element.loc.width, element.loc.height);
    }
    
    public void setPoint(Point p) {
        element.loc = new Location(p.x, p.y);
    }

}

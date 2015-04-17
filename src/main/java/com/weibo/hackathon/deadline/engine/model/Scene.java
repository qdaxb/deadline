package com.weibo.hackathon.deadline.engine.model;

import java.util.Collection;

import com.weibo.hackathon.deadline.common.Element;

public class Scene {
    
    public Size size;
    
    public Collection<Element> elements;
    
    public Collection<Element> getAllElements() {
        return elements;
    }
    
    public Player getPlayer() {
        return null;
    }

}

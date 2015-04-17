package com.weibo.hackathon.deadline.engine.model;

import java.util.ArrayList;
import java.util.Collection;

public class Scene {
    
    public Size size;
    
    public Collection<Element> elements = new ArrayList<Element>();
    
    public Collection<Element> getAllElements() {
        return elements;
    }
    
    public Player getPlayer() {
        return null;
    }

}

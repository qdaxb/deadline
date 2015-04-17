package com.weibo.hackathon.deadline.engine.model;

import java.util.ArrayList;
import java.util.Collection;

import com.weibo.hackathon.deadline.engine.GameResult;

public class Scene {
    
    public Size size = new Size(28, 78);
    
    public Collection<Element> elements = new ArrayList<Element>();
    
    public Collection<Element> getAllElements() {
        return elements;
    }
    
    public Player getPlayer() {
        return null;
    }

}

package com.weibo.hackathon.deadline.engine.model;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    public Element element;
    public GameObject father;
    public List<GameObject> children = new ArrayList<GameObject>();
}

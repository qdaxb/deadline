package com.weibo.hackathon.deadline.engine.model;

import java.util.ArrayList;

public class ModelAdapter {

    public static GameObject translate(final Scene scene) {
        GameObject go = new GameObject();
        go.father = null;
        go.element = new Block();
        go.element.size = scene.size;
        go.element.loc = new Location(1, 1);
        go.children = new ArrayList<GameObject>();
        for (Element elem : scene.elements) {
            GameObject ge = new GameObject();
            ge.father = go;
            ge.element = elem;
            go.children.add(ge);
        }
        return go;
    }

}

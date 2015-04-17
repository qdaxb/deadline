package com.weibo.hackathon.deadline.engine.model;

import java.util.ArrayList;

public class ModelAdapter {

    public static GameObject translate(final Scene scene) {
        GameObject go = new GameObject();
        go.father = null;
        go.element = new Element() {
            {
                this.size = scene.size;
                this.loc = new Location(0, 0);
            }
        };
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

package com.weibo.hackathon.deadline.engine.model;

import java.util.ArrayList;

public class ModelAdapter {

    private static final class EmptyElement extends Element {
//        public EmptyElement() {
//            loc = new Location(0, 0);
//        }
    }

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
        GameObject root = new GameObject();
        root.element = new EmptyElement();
        GameObject deadline = new GameObject();
        deadline.father = root;
        deadline.element = new AscIIImage();
        deadline.element.loc = new Location(4, 1);
        root.children.add(deadline);
        go.father = root;
        root.children.add(go);
        go.element.loc = new Location(1, 8);
        return root;
    }

}

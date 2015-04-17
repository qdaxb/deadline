package com.weibo.hackathon.deadline.engine.render;

import java.util.concurrent.LinkedBlockingDeque;

import com.weibo.hackathon.deadline.engine.model.GameObject;
import com.weibo.hackathon.deadline.engine.model.Location;

public class TextRender implements Render<String> {
    @Override
    public String render(GameObject root) {
        char[][] sceneArray = renderScene(root);
        return buildSceneString(sceneArray);
    }

    private char[][] renderScene(GameObject root) {
        char[][] sceneArray = buildEmptyScene();

        LinkedBlockingDeque<GameObject> elementQueue = new LinkedBlockingDeque<GameObject>();
        elementQueue.add(root);
        while (elementQueue.peek() != null) {
            GameObject obj = elementQueue.poll();
            GameObject father = obj.father;
            Location loc = new Location(obj.element.loc.height, obj.element.loc.width);
            if (father != null) {
                loc.height += father.element.loc.height;
                loc.width += father.element.loc.width;
            }
            char[][] shape = shapeRender.render(obj);
            if (shape != null) {
                insertShape(sceneArray, shape, loc);
            }
            if (obj.children != null) {
                elementQueue.addAll(obj.children);
            }
        }

        return sceneArray;
    }

    private char[][] buildEmptyScene() {
        char[][] sceneArray = new char[30][100];
        for (int i = 0; i < sceneArray.length; i++) {
            for (int j = 0; j < sceneArray[0].length; j++) {
                sceneArray[i][j] = ' ';
            }
        }
        return sceneArray;
    }

    private void insertShape(char[][] sceneArray, char[][] shape, Location loc) {
        for (int i = 0; i < sceneArray.length; i++) {
            for (int j = 0; j < sceneArray[0].length; j++) {
                boolean isHeightMatch = i > sceneArray.length - loc.height - shape.length && i <= sceneArray.length - loc.height;
                boolean isWidthMatch = j >= loc.width && j < loc.width + shape[0].length;
                if (isHeightMatch && isWidthMatch) {
                    char pixel = shape[i - (sceneArray.length - shape.length - loc.height) - 1][j - loc.width];
                    if (pixel != ' ' && pixel != '0') {
                        sceneArray[i][j] = pixel;
                    }
                }
            }
        }
    }

    private String buildSceneString(char[][] sceneArray) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sceneArray.length; i++) {
            for (int j = 0; j < sceneArray[0].length; j++) {
                builder.append(sceneArray[i][j]);
            }
            builder.append('\r');
            builder.append('\n');
        }
        return builder.toString();
    }

    private ShapeRender shapeRender = new ShapeRender();

}

package com.weibo.hackathon.deadline.engine.render;

import java.util.concurrent.LinkedBlockingDeque;

import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.Location;

public class TextRender implements Render<String> {
    @Override
    public String render(Element root) {
        char[][] sceneArray = renderScene(root);
        return buildSceneString(sceneArray);
    }
    
    private char[][] renderScene(Element root) {
        char[][] sceneArray = buildEmptyScene();

        LinkedBlockingDeque<Element> elementQueue = new LinkedBlockingDeque<>();
        elementQueue.add(root);
        while (elementQueue.peek() != null) {
            Element element = elementQueue.poll();
            insertShape(sceneArray, element.shape, element.loc);
            elementQueue.addAll(element.children);
        }

        return sceneArray;
    }

    private char[][] buildEmptyScene() {
        char[][] sceneArray = new char[20][80];
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
                boolean isHeightMatch = i >= loc.height && i < loc.height + shape.length;
                boolean isWidthMatch = j >= loc.width && j < loc.width + shape[0].length;
                if (isHeightMatch && isWidthMatch) {
                    sceneArray[i][j] = shape[i - loc.height][j - loc.width];
                }
            }
        }
    }

    private String buildSceneString(char[][] sceneArray) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sceneArray.length; i++) {
            for (int j = 0; j < sceneArray[0].length; j++) {
                builder.append(sceneArray[i][j] + " ");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append('\n');
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
 
}

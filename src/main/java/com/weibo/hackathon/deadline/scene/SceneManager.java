package com.weibo.hackathon.deadline.scene;

import java.util.ArrayList;

import com.weibo.hackathon.deadline.model.Element;
import com.weibo.hackathon.deadline.model.Layer;

public class SceneManager {
    public char[][] sceneArray = new char[20][80];
    public ArrayList<Layer> layers;

    public void buildScene() {
        for (Layer layer : layers) {
            for (Element element : layer.elements) {
                insertShape(element.shape, element.loc);
            }
        }
    }



    public void insertShape(char[][] shape, Location loc) {
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

    public static void printArray(char[][] sceneArray) {
        for (int i = 0; i < sceneArray.length; i++) {
            for (int j = 0; j < sceneArray[0].length; j++) {
                System.out.print(sceneArray[i][j] + " ");
            }
            System.out.println();
        }
    }


    public static class Location {
        public Location(int height, int width) {
            this.height = height;
            this.width = width;
        }

        public int height;
        public int width;
    }

    public static void main(String[] args) {
        char[][] sceneArray = new char[20][80];
        for (int i = 0; i < sceneArray.length; i++) {
            for (int j = 0; j < sceneArray[0].length; j++) {
                sceneArray[i][j] = '_';
            }
        }

        SceneManager m = new SceneManager();
        m.sceneArray = sceneArray;

        char[][] shape = new char[2][2];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                shape[i][j] = '*';
            }
        }
        Location loc = new Location(5, 5);
        m.insertShape(shape, loc);

        SceneManager.printArray(m.sceneArray);
    }
}

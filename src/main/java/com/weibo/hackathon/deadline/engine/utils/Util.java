package com.weibo.hackathon.deadline.engine.utils;

public class Util {
    public static void printArray(char[][] sceneArray) {
        for (int i = 0; i < sceneArray.length; i++) {
            for (int j = 0; j < sceneArray[0].length; j++) {
                System.out.print(sceneArray[i][j] + " ");
            }
            System.out.println();
        }
    }
}

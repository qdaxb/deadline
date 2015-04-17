package com.weibo.hackathon.deadline.render;

import java.io.IOException;
import java.io.OutputStream;

import com.weibo.hackathon.deadline.scene.Scene;

public class StringRender {
    private Scene scene;
    private OutputStream steam;

    public void renderStringOutput() {
        char[][] sceneArray = scene.getSceneArray();
        printArray(sceneArray, steam);
    }

    public static void printArray(char[][] sceneArray, OutputStream stream) {
        for (int i = 0; i < sceneArray.length; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < sceneArray[0].length; j++) {
                builder.append(sceneArray[i][j] + " ");
            }
            builder.deleteCharAt(builder.length() - 1);
            try {
                stream.write(builder.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

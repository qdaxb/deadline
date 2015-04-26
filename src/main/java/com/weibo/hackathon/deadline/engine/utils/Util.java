package com.weibo.hackathon.deadline.engine.utils;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class Util {
    public static void printArray(char[][] sceneArray) {
        for (int i = 0; i < sceneArray.length; i++) {
            for (int j = 0; j < sceneArray[0].length; j++) {
                System.out.print(sceneArray[i][j]);
            }
            System.out.println();
        }
    }

	public static <E> Set<E> identitySet() {
		IdentityHashMap<E, Boolean> map = new IdentityHashMap<E, Boolean>();
		return Collections.newSetFromMap(map);
	}
}

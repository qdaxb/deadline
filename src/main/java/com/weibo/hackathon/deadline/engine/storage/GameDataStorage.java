package com.weibo.hackathon.deadline.engine.storage;


import com.weibo.hackathon.deadline.engine.utils.Constants;

public class GameDataStorage {
    private GameDataStorage() {
        storage = new RedisStorage();
    }

    public static GameDataStorage getInstance() {
        return instance;
    }

    public int getMaxTime() {
        String max = getMax();
        if (max != null && !"".equals(max)) {
            return Integer.parseInt(max.split(",")[1]);
        } else {
            return 0;
        }
    }

    public String getMax() {
        try {
            return (String) storage.get(Constants.KEY_MAX);
        } catch (Exception e) {
            return (String) localStorage.get(Constants.KEY_MAX);
        }
    }

    public void setMax(String max) {
        try {
            storage.set(Constants.KEY_MAX, max);
        } catch (Exception e) {
            localStorage.set(Constants.KEY_MAX, max);
        }
    }

    private static final GameDataStorage instance = new GameDataStorage();

    private Storage<String> storage;
    private LocalStorage localStorage = new LocalStorage();
}

package com.weibo.hackathon.deadline.engine.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalStorage implements Storage<Object> {

    @Override
    public Object get(String key) {
        return data.get(key);
    }

    @Override
    public void set(String key, Object value) {
        data.put(key, value);
    }

    private Map<String, Object> data = new ConcurrentHashMap<String, Object>();

}

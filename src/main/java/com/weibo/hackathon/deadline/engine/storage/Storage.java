package com.weibo.hackathon.deadline.engine.storage;

public interface Storage<T> {
    T get(String key);

    void set(String key, T value);
}

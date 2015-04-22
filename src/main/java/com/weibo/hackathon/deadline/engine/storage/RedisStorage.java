package com.weibo.hackathon.deadline.engine.storage;

import redis.clients.jedis.Jedis;

public class RedisStorage implements Storage<String> {
    public RedisStorage() {
        jedis = new Jedis("10.77.108.128", 6401, 1000);
    }

    @Override
    public synchronized String get(String key) {
        return jedis.get(key);
    }

    @Override
    public synchronized void set(String key, String value) {
        jedis.set(key, value);
    }

    private Jedis jedis;
}

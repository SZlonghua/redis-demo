package com.example.redis.lock;

public interface RedisLockChange {


    String lock(String key, long expire, long timeout);

    boolean unlock(String key, String value);
}

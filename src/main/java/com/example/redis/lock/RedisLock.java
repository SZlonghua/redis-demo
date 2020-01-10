package com.example.redis.lock;

public interface RedisLock {

    String lock() throws InterruptedException;

    Boolean unlock(String value);
}

package com.example.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

@Slf4j
public class JedisTest {

    @Test
    void contextLoads() {
        Jedis jedis = new Jedis("localhost");
        jedis.set("foo", "bar");
        String value = jedis.get("foo");
        log.info(value);
    }
}

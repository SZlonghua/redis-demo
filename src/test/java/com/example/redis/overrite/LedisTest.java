package com.example.redis.overrite;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class LedisTest {
    @Test
    public void test(){
        log.debug("dd");
        Ledis ledis = new Ledis("127.0.0.1",6379);
        ledis.set("ces","333333");

    }

}

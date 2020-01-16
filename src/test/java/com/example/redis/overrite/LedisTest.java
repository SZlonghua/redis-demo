package com.example.redis.overrite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LedisTest {
    @Test
    public void test(){

        Ledis ledis = new Ledis("127.0.0.1",6379);
        ledis.set("ces","333333");
    }

}

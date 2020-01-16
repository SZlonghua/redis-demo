package com.example.redis;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/*@SpringBootTest*/
class RedisDemoApplicationTests {

    @Test
    void contextLoads() {
        Object obj = new Object();
        Object obj1 = new Object();
        Assert.assertSame("liaotao",obj,obj);
        Assert.assertNotNull("dddd",obj);
        Assert.assertEquals(obj,obj);
    }


}

package com.example.redis.controller;

import com.alibaba.fastjson.JSON;
import com.example.redis.covert.IntegerCovertString;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Api
@Slf4j
public class RedisController {

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping("/hello")
    @ApiOperation(value="hello")
    public String hello(){
        log.info("hello world 9999999");
        return "hello world!";
    }

    @RequestMapping("/set")
    @ApiOperation(value="set")
    public String set(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("liao","22");
        return "set sucess";
    }
    @RequestMapping("/get")
    @ApiOperation(value="get")
    public String get(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String liao = (String)valueOperations.get("liao");
        log.info(liao);
        return liao;
    }
    @RequestMapping("/pip")
    @ApiOperation(value="pip")
    public String pip(){
        List list = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                byte[] key1 = "mykey1".getBytes();
                byte[] value1 = "字符串value".getBytes();
                connection.set(key1,value1);

//                3.2一个批量mset操作
                Map<byte[],byte[]> tuple = new HashMap<>();
                tuple.put("m_mykey1".getBytes(),"m_value1".getBytes());
                tuple.put("m_mykey2".getBytes(),"m_value2".getBytes());
                tuple.put("m_mykey3".getBytes(),"m_value3".getBytes());
                connection.mSet(tuple);

//                 3.3一个get操作
                connection.get("m_mykey2".getBytes());
                return null;
            }
        });
        log.info(JSON.toJSONString(list));
        return "d";
    }


    @RequestMapping("/pub")
    @ApiOperation(value="pub")
    public String pub(){
        redisTemplate.convertAndSend("user","sssssssss");
        return "sucess";
    }

    @RequestMapping("/expire")
    @ApiOperation(value="expire")
    public String expire(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("expire","liaotao",20, TimeUnit.SECONDS);
        return "expire sucess";
    }

    @RequestMapping("/tra")
    @ApiOperation(value="tra")
    public String tra(){

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String key = (String)valueOperations.get("liao");
        log.info("key:"+key);


        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.multi();

        /*String myKey = (String)valueOperations.get("liao");
        log.info("myKey:"+myKey);
*/
        List list = new ArrayList();
        list.add("liao");
        List list1 = valueOperations.multiGet(list);
        valueOperations.set("liao",new IntegerCovertString(key).add(5));
        redisTemplate.exec();
        return "Transaction sucess";
    }

}

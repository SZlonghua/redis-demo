package com.example.redis.controller;


import com.example.redis.lock.RedisLock;
import com.example.redis.lock.RedisLockImpl;
import com.example.redis.util.SpringContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api
@Slf4j
public class LockController {

    @Autowired
    RedisTemplate redisTemplate;



    @RequestMapping("/lock")
    @ApiOperation(value="lock")
    public String lock(){
        RedisLock lock = new RedisLockImpl(redisTemplate);
        String token = null;
        try{
            token = lock.lock();
            if(token != null) {
                log.info("我拿到了锁哦");
                // 执行业务代码
            } else {
                log.info("我没有拿到锁唉");
            }
        } catch (InterruptedException e) {
            log.info(e.getStackTrace().toString());
        } finally {
            if(token!=null) {
                lock.unlock(token);
            }
        }
        return token;
    }
}

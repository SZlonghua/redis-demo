package com.example.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.UUID;

@Slf4j
public class RedisLockImpl implements RedisLock {

    private RedisTemplate redisTemplate;

    public RedisLockImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String LOCKKEY = "lockKey";

    private static final String unlockScript =
                    "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                    "    return redis.call(\"del\",KEYS[1])\n" +
                    "else\n" +
                    "    return 0\n" +
                    "end";

    private static final String lockScript = "return redis.call('SET', KEYS[1], ARGV[1], 'NX', 'PX', 30000)";


    private String tryLock(String key,String arg) {
        return (String)execute(lockScript,String.class,key,arg);
    }

    private Long unLock(String key,String value) {
        return (Long)execute(unlockScript,Long.class,key,value);
    }

    private Object execute(String script,Class returnType,String key,String arg) {
        // 指定 lua 脚本，并且指定返回值类型
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        return  redisTemplate.execute(new DefaultRedisScript<>(script,returnType), Collections.singletonList(key), arg);
    }

    @Override
    public String lock() throws InterruptedException {
        String value = UUID.randomUUID().toString();
        String result = null;
        long startTime = System.currentTimeMillis();
        while (!"OK".equals((result= tryLock(LOCKKEY, value)))){
            log.info("token:"+result);
            Thread.sleep(50);
            //防止死循环，过了超时时间返回null
            if((System.currentTimeMillis()-startTime) > (30000-50))
                return null;
        }
        return value;
    }

    @Override
    public Boolean unlock(String value) {
        Long lock = unLock(LOCKKEY, value);
        return lock!=null&&lock>0;
    }
}

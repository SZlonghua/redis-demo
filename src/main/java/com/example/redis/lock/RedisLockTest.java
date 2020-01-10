package com.example.redis.lock;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisLockTest implements RedisLockChange {

    /**
     * 解锁脚本，原子操作
     */
    private static final String unlockScript =
                    "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                    "    return redis.call(\"del\",KEYS[1])\n" +
                    "else\n" +
                    "    return 0\n" +
                    "end";

    private static final String lockScript = "SET KEY[1] ARGV[1] NX PX 30000";

    private RedisTemplate redisTemplate;

    public RedisLockTest(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 加锁，有阻塞
     * @param name
     * @param expire
     * @param timeout
     * @return
     */
    public String lock(String name, long expire, long timeout){
        long startTime = System.currentTimeMillis();
        String token;
        do{
            token = tryLock(name, expire);
            if(token == null) {
                if((System.currentTimeMillis()-startTime) > (timeout-50))
                    break;
                try {
                    Thread.sleep(50); //try 50 per sec
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }while(token==null);

        return token;
    }

    /**
     * 加锁，无阻塞
     * @param name
     * @param expire
     * @return
     */
    private String tryLock(String name, long expire) {
        String token = UUID.randomUUID().toString();
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection conn = factory.getConnection();
        try{
            Boolean result = conn.set(name.getBytes(Charset.forName("UTF-8")), token.getBytes(Charset.forName("UTF-8")),
                    Expiration.from(expire, TimeUnit.MILLISECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
            if(result!=null && result)
                return token;
        }finally {
            RedisConnectionUtils.releaseConnection(conn, factory,false);
        }
        return null;
    }

    private Long tryLock(String key,String value) {
        // 指定 lua 脚本，并且指定返回值类型
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(lockScript,Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = (Long)redisTemplate.execute(redisScript, Collections.singletonList(key), UUID.randomUUID().toString());

        return result;
    }

    /**
     * 解锁
     * @param name
     * @param token
     * @return
     */
    public boolean unlock(String name, String token) {
        byte[][] keysAndArgs = new byte[2][];
        keysAndArgs[0] = name.getBytes(Charset.forName("UTF-8"));
        keysAndArgs[1] = token.getBytes(Charset.forName("UTF-8"));
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection conn = factory.getConnection();
        try {
            Long result = (Long)conn.scriptingCommands().eval(unlockScript.getBytes(Charset.forName("UTF-8")), ReturnType.INTEGER, 1, keysAndArgs);
            if(result!=null && result>0)
                return true;
        }finally {
            RedisConnectionUtils.releaseConnection(conn, factory,false);
        }

        return false;
    }


    public Boolean executeScripting(String script, ReturnType returnType, byte[]... keysAndArgs){
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection conn = factory.getConnection();
        try {
            Integer result = (Integer)executeScripting(conn,unlockScript,ReturnType.INTEGER,1,keysAndArgs);
            if(result!=null && result>0)
                return true;
        }finally {
            RedisConnectionUtils.releaseConnection(conn, factory,false);
        }
        return false;
    }

    private Integer executeScripting(RedisConnection conn, String script, ReturnType returnType,int numKeys, byte[]... keysAndArgs){
        return (Integer) conn.scriptingCommands().eval(script.getBytes(Charset.forName("UTF-8")), returnType, numKeys, keysAndArgs);
    }
}

package com.example.redis.receiver;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserReceiver {
    public void receiveMessage(Object message) {

        // TODO 这里是收到通道的消息之后执行的方法
        log.info("receiver message:"+ JSON.toJSONString(message));
    }
}

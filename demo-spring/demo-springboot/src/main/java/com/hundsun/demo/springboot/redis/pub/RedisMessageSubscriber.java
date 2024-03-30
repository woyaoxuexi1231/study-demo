package com.hundsun.demo.springboot.redis.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageSubscriber implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("Received message: " + message);
    }
}

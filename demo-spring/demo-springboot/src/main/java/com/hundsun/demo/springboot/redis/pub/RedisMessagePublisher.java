package com.hundsun.demo.springboot.redis.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value = "/redispush")
@Component
public class RedisMessagePublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ChannelTopic channelTopic;

    @RequestMapping(value = "/publish")
    public void publish(String message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
        System.out.println("Published message: " + message);
    }
}

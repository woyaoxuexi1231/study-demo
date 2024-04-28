package com.hundsun.demo.springboot.redis.pub;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping(value = "/redispush")
@Component
public class RedisMessagePublisher {

    @Autowired
    @Qualifier(value = "stringObjRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ChannelTopic channelTopic;

    @RequestMapping(value = "/publish")
    public void publish() {
        redisTemplate.convertAndSend(channelTopic.getTopic(), DateUtil.date().toString());
        log.info("Published message: {}", DateUtil.date());
    }
}

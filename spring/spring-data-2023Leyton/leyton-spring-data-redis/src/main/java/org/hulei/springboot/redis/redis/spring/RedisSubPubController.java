package org.hulei.springboot.redis.redis.spring;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


@Configuration
@Slf4j
@RestController
@RequestMapping(value = "/redisChannelSubscribe")
@Component
public class RedisSubPubController {


    /*========================================== redis消息队列 =======================================*/

    /**
     * 创建一个消息通道类, 可以用这个类来发送和接收消息
     * <p>
     * ChannelTopic 是 Spring Data Redis 中用于表示消息通道的类。
     * 它提供了一种简单的方式来定义和操作 Redis 中的消息通道。
     * 通常在使用 Redis 进行消息发布和订阅时，你需要指定一个通道（channel）来发送和接收消息。ChannelTopic 就是用来表示这个通道的。
     *
     * @return ChannelTopic
     */
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("spring-channel-mq");
    }

    @Autowired
    public void redisMessageListenerContainer(RedisMessageListenerContainer redisMessageListenerContainer) {
        // 用于向容器中添加消息监听器，并指定要监听的主题（Topic）。
        redisMessageListenerContainer.addMessageListener(
                (message, pattern) -> {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    String channel = serializer.deserialize(message.getChannel());
                    String msg = serializer.deserialize(message.getBody());
                    log.info("redis MessageListener 收到消息： channel: {}, 信道(主题) {}, 消息为: {}", channel, Objects.isNull(pattern) ? null : new String(pattern, StandardCharsets.UTF_8), msg);
                },
                channelTopic());
    }

    /**
     * redis操作类
     */
    @Autowired
    @Qualifier(value = "strObjRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 消息通道类,这里通过这个类进行发送消息
     */
    @Autowired
    private ChannelTopic channelTopic;

    @RequestMapping(value = "/publish")
    public void publish() {
        redisTemplate.convertAndSend(channelTopic.getTopic(), DateUtil.date().toString());
        log.info("使用 redisTemplate 发消息, 主题: {}, msg: {}", channelTopic.getTopic(), DateUtil.date());
    }

}

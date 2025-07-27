package org.hulei.springboot.rabbitmq.spring.utils;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author hulei
 * @since 2025/7/27 20:44
 */

@Slf4j
public class LogUtil {

    public static void log(Message msg) {
        log.info("交换机：{}，路由键：{}，队列：{}，消息：{}",
                msg.getMessageProperties().getReceivedExchange(),
                msg.getMessageProperties().getReceivedRoutingKey(),
                msg.getMessageProperties().getConsumerQueue(),
                new String(msg.getBody(), StandardCharsets.UTF_8)
        );
    }
}

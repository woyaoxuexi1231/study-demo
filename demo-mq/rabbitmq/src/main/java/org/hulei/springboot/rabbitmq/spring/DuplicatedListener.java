package org.hulei.springboot.rabbitmq.spring;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei
 * @since 2024/9/17 23:04
 */

@Slf4j
@Component
public class DuplicatedListener {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String PROCESSED_KEY_PREFIX = "processed_msg:";

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "duplicated-queue", autoDelete = "false", durable = "true", exclusive = "false"),
                    exchange = @Exchange(value = MQConfig.NORMAL_TOPIC_EXCHANGE, type = "topic"),
                    key = "normal.duplicate.key"
            ))
    public void handleMessage(Message message, Channel channel) {

        try {

            String msgId = message.getMessageProperties().getMessageId(); // 确保发送时设置了 messageId
            String string = new String(message.getBody(), StandardCharsets.UTF_8);

            if (msgId == null) {
                System.out.println("消息缺少 ID，拒绝处理");
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            }


            String redisKey = PROCESSED_KEY_PREFIX + msgId;

            Boolean isNew = redisTemplate.opsForValue().setIfAbsent(redisKey, "1", 1, TimeUnit.DAYS);

            if (Boolean.FALSE.equals(isNew)) {
                System.out.println("消息已处理，跳过重复处理: " + msgId);
            }

            // 业务处理逻辑
            System.out.println("处理消息: " + msgId);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                redisTemplate.delete(PROCESSED_KEY_PREFIX + message.getMessageProperties().getMessageId());
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            } catch (IOException ex) {
                log.info("消息处理失败，并且尝试重新入列也失败了！{}", message.getMessageProperties().getMessageId());
            }
            log.error(e.getMessage(), e);
        }
    }

}

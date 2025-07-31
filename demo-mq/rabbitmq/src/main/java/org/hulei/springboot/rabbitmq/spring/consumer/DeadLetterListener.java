package org.hulei.springboot.rabbitmq.spring.consumer;

import com.rabbitmq.client.Channel;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.hulei.springboot.rabbitmq.spring.utils.LogUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2025/7/27 22:07
 */

@Component
public class DeadLetterListener {

    @RabbitListener(queues = MQConfig.DEAD_QUEUE_NAME)
    public void receive(Message message, Channel channel) throws Exception {
        try {
            LogUtil.log(message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

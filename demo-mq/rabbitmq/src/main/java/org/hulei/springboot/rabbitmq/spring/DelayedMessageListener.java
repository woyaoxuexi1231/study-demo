package org.hulei.springboot.rabbitmq.spring;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author hulei
 * @since 2025/8/22 15:27
 */

@Component
public class DelayedMessageListener {

    @RabbitListener(queues = "delay_queue")
    public void receive(String String, Message message, Channel channel) {
        try {
            System.out.println("收到延迟消息：" + message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

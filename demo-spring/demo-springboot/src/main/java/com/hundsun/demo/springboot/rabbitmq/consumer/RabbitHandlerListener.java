package com.hundsun.demo.springboot.rabbitmq.consumer;

import com.hundsun.demo.spring.mq.rabbit.config.MQConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author hulei42031
 * @since 2024-03-28 14:57
 */

@Component
@Slf4j
@RabbitListener(queues = MQConfig.TOPIC_MASTER_QUEUE)
public class RabbitHandlerListener {


    @org.springframework.amqp.rabbit.annotation.RabbitHandler
    public void receive1(byte[] msgBytes, Message msg, Channel channel) {
        // 如果发送端发送的是byte[],那么就会触发这个方法
        System.out.println(String.format("RabbitHandlerListener#receive1 Received string messageBytes: %s, msgBytes: %s", Arrays.toString(msgBytes), msg));
        try {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

    @org.springframework.amqp.rabbit.annotation.RabbitHandler
    public void receive2(String msgStr, Message msg, Channel channel) {
        // 1. 方法参数内必须要有一个String类型的参数,不然会报错Caused by: org.springframework.amqp.AmqpException: No method found for class java.lang.String
        // 2. 不能有多个RabbitHandler方法并且都能处理同一种类型的消息(比如这里的String类型),Caused by: org.springframework.amqp.AmqpException: Ambiguous methods for payload type: class java.lang.String: handleMessage and receive2
        System.out.println(String.format("RabbitHandlerListener#receive2 Received string messageStr: %s, msgBytes: %s", msgStr, msg));
        try {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

    // @RabbitHandler
    public void handleMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        System.out.println("RabbitHandlerListener#handleMessage Received string message: " + message);
        try {
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }

}

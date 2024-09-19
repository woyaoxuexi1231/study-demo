package org.hulei.springboot.rabbitmq.basic.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: MessageConsumer
 * @description:
 * @author: h1123
 * @createDate: 2023/2/28 23:20
 */

@Slf4j
public class MsgPushConsumerB extends MsgConsumer {

    public MsgPushConsumerB(Connection connection, String queueName) {
        super(connection, queueName);
    }

    @Override
    public void run() {
        try {
            Channel channel = connection.createChannel();
            boolean autoAck = false;
            channel.basicQos(1);
            channel.basicConsume(
                    queueName,
                    autoAck,
                    "",
                    (consumerTag, message) -> {
                        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
                        log.info("topic: {}, 收到消息: {}", message.getEnvelope().getRoutingKey(), msg);
                        if (msg.equals("exit")) {
                            // requeue - 如果拒绝的消息应该重新排队而不是丢弃/死信, 则为 true
                            channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                        } else {
                            /*
                            如果此队列只有一个消费者, 此处并不ack消息,prefetchCount=1, 那么rabbitmq将一直等待此消费者ack, 但却收不到, 最终导致一直阻塞
                            消费者确认 autoAck = false/true
                            肯定确认 - BasicAck
                            否定确认 - BasicNack、BasicReject, basicNack可以批量拒绝多条消息, 而 basicReject一次只能拒绝一条消息
                            multiple - false 表示只确认 DelivertTag 这条消息, true 表示确认 小于等于 DelivertTag 的所有消息(批量确认)
                             */
                            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                        }
                    },
                    consumerTag -> log.info("消费消息被中断!"),
                    (consumerTag, sig) -> log.error("consumerTag: {}, ShutdownSignalException: ", consumerTag, sig)
            );
        } catch (Exception e) {
            log.error("接收消息异常! ", e);
        }
    }
}


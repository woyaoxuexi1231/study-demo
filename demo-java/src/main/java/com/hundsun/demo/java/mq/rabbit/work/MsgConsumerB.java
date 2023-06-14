package com.hundsun.demo.java.mq.rabbit.work;

import com.hundsun.demo.java.mq.rabbit.callback.MsgDeliverCallbackB;
import com.hundsun.demo.java.mq.rabbit.callback.MyCancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: MessageConsumer
 * @description:
 * @author: h1123
 * @createDate: 2023/2/28 23:20
 */

@Slf4j
public class MsgConsumerB extends Thread {

    private final Connection connection;

    private final String queueName;

    public MsgConsumerB(Connection connection, String queueName) {
        this.connection = connection;
        this.queueName = queueName;
    }

    @Override
    public void run() {

        try {

            Channel channel = connection.createChannel();
            boolean autoAck = false;
            channel.basicQos(2);
            channel.basicConsume(
                    queueName,
                    autoAck,
                    "",
                    new MsgDeliverCallbackB(channel),
                    new MyCancelCallback());

        } catch (Exception e) {
            log.error("接收消息异常! ", e);
        }
    }
}


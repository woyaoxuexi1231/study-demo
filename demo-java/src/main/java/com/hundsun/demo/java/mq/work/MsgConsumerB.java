package com.hundsun.demo.java.mq.work;

import com.hundsun.demo.java.mq.callback.MsgDeliverCallbackB;
import com.hundsun.demo.java.mq.callback.MyCancelCallback;
import com.hundsun.demo.java.mq.config.MQConfig;
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

    public MsgConsumerB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {


        try {

            Channel channel = connection.createChannel();
            boolean autoAck = false;
            channel.basicQos(2);
            channel.basicConsume(
                    MQConfig.QUEUE_NAME,
                    autoAck,
                    "",
                    new MsgDeliverCallbackB(channel, autoAck),
                    new MyCancelCallback());

        } catch (Exception e) {
            log.error("接收消息异常! ", e);
        }
    }
}


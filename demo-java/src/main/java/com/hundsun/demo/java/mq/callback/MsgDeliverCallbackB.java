package com.hundsun.demo.java.mq.callback;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: MyDeliverCallback
 * @description:
 * @author: h1123
 * @createDate: 2023/2/28 23:21
 */

@Slf4j
public class MsgDeliverCallbackB implements DeliverCallback {

    private final Channel channel;

    public MsgDeliverCallbackB(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void handle(String s, Delivery delivery) throws IOException {

        log.info("收到消息: {}", new String(delivery.getBody(), "UTF-8"));

        try {
            Thread.sleep(0 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /*
        消费者确认 autoAck = false/true
        肯定确认 - BasicAck
        否定确认 - BasicNack、BasicReject, basicNack可以批量拒绝多条消息, 而 basicReject一次只能拒绝一条消息
        multiple - false 表示只确认 DelivertTag 这条消息, true 表示确认 小于等于 DelivertTag 的所有消息(批量确认)
         */
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

    }
}


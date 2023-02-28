package com.hundsun.demo.java.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: MyDeliverCallback
 * @description:
 * @author: h1123
 * @createDate: 2023/2/28 23:21
 */

@Slf4j
public class MyDeliverCallback implements DeliverCallback {

    private CountDownLatch count;

    private Channel channel;

    private boolean autoAck;

    public MyDeliverCallback(Channel channel, CountDownLatch count, boolean autoAck) {
        this.count = count;
        this.channel = channel;
        this.autoAck = autoAck;
    }

    /**
     * @param s        与使用者关联的使用者标记
     * @param delivery 传递的消息
     * @throws IOException ex
     */
    @Override
    public void handle(String s, Delivery delivery) throws IOException {
        try {
            log.info("消息体的内容: {}", new String(delivery.getBody(), "UTF-8"));

            // try {
            //     Thread.sleep(1000);
            // } catch (InterruptedException e) {
            //     throw new RuntimeException(e);
            // }
            if (!autoAck) {
                /*
                消费者确认 autoAck = false/true
                肯定确认 - BasicAck
                否定确认 - BasicNack、BasicReject, basicNack可以批量拒绝多条消息, 而 basicReject一次只能拒绝一条消息
                multiple - false 表示只确认 DelivertTag 这条消息, true 表示确认 小于等于 DelivertTag 的所有消息(批量确认)
                 */
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        } finally {
            // count.countDown();
        }
    }
}


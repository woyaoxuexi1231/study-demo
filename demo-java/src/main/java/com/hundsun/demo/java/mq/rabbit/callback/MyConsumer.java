package com.hundsun.demo.java.mq.rabbit.callback;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: MyConsumer
 * @description:
 * @author: h1123
 * @createDate: 2023/2/28 23:16
 */

public class MyConsumer extends DefaultConsumer {

    /*
     * 回调接口
     * 也是回调, 包含了各种应答回调函数
     * 相较于 DeliverCallback 和 CancelCallback, 后两个把前者的部分接口抽离出来
     */

    public MyConsumer(Channel channel, CountDownLatch count) {
        super(channel);
        this.count = count;
    }

    private CountDownLatch count;

    /*
     消费方法
     * consumerTag - 消费者的标签, 在channel.basicConsume()去指定
     * envelope - 消息包的内容, 可从中获取消息id, 消息RoutingKey, 交换机, 消息和重传标志(收到消息失败后是否需要重新发送)
     * properties - 消息的属性
     * body - 消息的内容
     */
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("路由键: " + envelope.getRoutingKey());
        System.out.println("内容类型: " + properties.getContentType());
        this.getChannel().basicAck(envelope.getDeliveryTag(), false);
        System.out.println("消息体的内容: ");
        System.out.println(new String(body, "UTF-8"));
        count.countDown();
    }
}

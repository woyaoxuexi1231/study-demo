package com.hundsun.demo.java.mq.work;

import com.hundsun.demo.java.mq.callback.MsgDeliverCallbackA;
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
public class MsgConsumerA extends Thread {

    private final Connection connection;

    public MsgConsumerA(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {


        try {

            Channel channel = connection.createChannel();

            // 手动应答
            boolean autoAck = false;

            /*
             开始获取消息，push模式
             * queue
             * autoAck, 上面开启了手动应答basicAck，所以这里是false；当没basicAck一般为true
             * consumerTag 消费者标签, 用来区分多个消费者
             * noLocal 设置为 true, 表示不能将同一个 Connection 中生产者发送的消息传递给这个 Connection 中的消费者
             * exclusive 是否排他
             * arguments 消费者的参数
             * DeliverCallback 当一个消息发送过来后的回调
             * CancelCallback 当一个消费者取消订阅时的回调接口, 消费消息被中断
             * Consumer 消费者对象的回调接口

             this.basicConsume(queue, autoAck, consumerTag, false, false, (Map)null, callback);
             */
            channel.basicConsume(MQConfig.QUEUE_NAME, autoAck, "", new MsgDeliverCallbackA(channel, autoAck), new MyCancelCallback());

        } catch (Exception e) {
            log.error("接收消息异常! ", e);
        }
    }
}


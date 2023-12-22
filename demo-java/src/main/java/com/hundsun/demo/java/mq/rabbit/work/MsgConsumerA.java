package com.hundsun.demo.java.mq.rabbit.work;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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
public class MsgConsumerA extends Thread {

    private final Connection connection;

    private final String queueName;

    public MsgConsumerA(Connection connection, String queueName) {
        this.connection = connection;
        this.queueName = queueName;
    }

    @Override
    public void run() {

        try {

            Channel channel = connection.createChannel();

            // 手动应答
            boolean autoAck = false;

            /*
            basicQos(int prefetchSize, int prefetchCount, boolean global)
            prefetchSize - 服务器将提供的最大内容量(以八位字节为单位), 如果无限制, 则为 0, 消息大小限制, 一般设置为 0, 消费端不做限制
            prefetchCount - 服务器将传递的最大邮件数, 如果无限制, 则为 0, 告诉 rabbitmq 不要一次性给消费者推送大于 N 个消息, 即一旦有 N 个消息还没有 ack, 则该 consumer 将 block(阻塞), 直到有消息ack
            global - 是否将上面的设置应用于整个通道
             */
            channel.basicQos(1);
            /*
             开始获取消息，push模式
             * queue
             * autoAck, 上面开启了手动应答basicAck，所以这里是false；当没basicAck一般为true
             * consumerTag 消费者标签, 用来区分多个消费者, 如果你在订阅消息队列时将 consumerTag 设定为空，那么消息代理（如RabbitMQ）会为消费者生成一个唯一的 consumerTag，并将其返回给消费者应用程序。这个生成的 consumerTag 通常是一个唯一的字符串，用于标识特定的消费者，以便进行后续的消息交付和管理。
             * noLocal 设置为 true, 表示不能将同一个 Connection 中生产者发送的消息传递给这个 Connection 中的消费者
             * exclusive 是否排他
             * arguments 消费者的参数
             * DeliverCallback 当一个消息发送过来后的回调
             * CancelCallback 当一个消费者取消订阅时的回调接口, 消费消息被中断
             * Consumer 消费者对象的回调接口

             this.basicConsume(queue, autoAck, consumerTag, false, false, (Map)null, callback);
             */
            // channel.basicConsume(
            //         queueName,
            //         autoAck,
            //         "",
            //         new MsgDeliverCallbackA(channel),
            //         new MyCancelCallback());
            channel.basicConsume(
                    queueName,
                    autoAck,
                    "",
                    new DefaultConsumer(channel) {
                        /**
                         *
                         * @param consumerTag the <i>consumer tag</i> associated with the consumer
                         * @param envelope packaging data for the message, envelope 参数是一个包含了消息的交付信息的对象
                         * @param properties content header data for the message
                         * @param body the message body (opaque, client-specific byte array)
                         * @throws IOException
                         */
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            log.info("consumerTag: {}", consumerTag);
                            log.info("envelope: {}", envelope);
                            log.info("properties: {}", properties);
                            log.info("body: {}", new String(body, StandardCharsets.UTF_8));
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        }
                    });

        } catch (Exception e) {
            log.error("接收消息异常! ", e);
        }
    }
}


package com.hundsun.demo.java.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: MessageConsumer
 * @description:
 * @author: h1123
 * @createDate: 2023/2/28 23:20
 */

@Slf4j
public class MessageConsumer extends Thread {

    private final ConnectionFactory FACTORY;

    public MessageConsumer(ConnectionFactory FACTORY) {
        this.FACTORY = FACTORY;
    }

    @Override
    public void run() {

        // try {
        //     Thread.sleep(10000);
        // } catch (InterruptedException e) {
        //     throw new RuntimeException(e);
        // }
        Connection connection = null;
        Channel channel = null;

        CountDownLatch count = new CountDownLatch(1);
        try {

            connection = FACTORY.newConnection();
            channel = connection.createChannel();

            // 交换机和路由键
            String exchangeName = "hello-exchange";
            String routingKey = "testRoutingKey";

                /*
                 声明队列
                 * String queue, 队列
                 * boolean durable, 是否持久化。代表队列在服务器重启后是否还存在
                 * boolean exclusive, 是否排他性队列。排他性队列只能在声明它的 Connection 中使用（可以在同一个Connection的不同的channel中使用），连接断开时自动删除
                 * boolean autoDelete, 是否自动删除队列。如果为true，至少有一个消费者连接到这个队列，之后所有与这个队列连接的消费者都断开时，队列会自动删除。
                 * Map<String,Object> arguments， 队列其他属性

                 这里使用一个默认配置
                 this.queueDeclare("", false, true, true, (Map)null);
                 */
            String queueName = "hello-consumer";

            // 把队列绑定到交换机上
            channel.queueDeclare(queueName, true, false, false, null).getQueue();
            channel.queueBind(queueName, exchangeName, routingKey);

            boolean autoAck = false;
            String consumerTag = "";

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

            log.info("准备接收消息....");
            channel.basicConsume(queueName, autoAck, consumerTag, new MyDeliverCallback(channel, count, autoAck), new MyCancelCallback(count));

        } catch (Exception e) {
            log.error("接收消息异常! ", e);
        } finally {
            try {
                count.await();
                if (channel != null) {
                    channel.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                log.error("连接关闭异常! ", e);
            }
        }
    }
}


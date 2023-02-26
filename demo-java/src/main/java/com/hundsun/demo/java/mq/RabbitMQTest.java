package com.hundsun.demo.java.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: RabbitMQTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/26 18:57
 */

@Slf4j
public class RabbitMQTest {


    private static final ConnectionFactory FACTORY;

    static {
        FACTORY = new ConnectionFactory();
        FACTORY.setUsername(MQConfig.RABBITMQ_USERNAME);
        FACTORY.setPassword(MQConfig.RABBITMQ_PASSWORD);
        // 地址
        FACTORY.setHost(MQConfig.RABBITMQ_HOST);
        FACTORY.setPort(MQConfig.RABBITMQ_PORT);
        FACTORY.setVirtualHost("/");
    }

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        new MessageProducer().start();
        // 确保消息被生产
        Thread.sleep(1000);
        new MessageConsumer().start();

    }

    static class MessageProducer extends Thread {

        @Override
        public void run() {

            Connection connection = null;
            Channel channel = null;

            try {

                connection = FACTORY.newConnection();
                channel = connection.createChannel();

                /*
                 声明交换机
                 * String exchange, 交换机
                 * String type, 交换机类型（direct，topic，fanout）
                 * boolean durable, 是否持久化。代表交换机在服务器重启后是否还存在
                 * boolean autoDelete, 自动删除
                 * Map<String,Object> arguments，交换机其他属性，如x-message-ttl，x-max-length
                 */
                String exchangeName = "hello-exchange";

                channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true, false, null);


                // 路由规则
                String routingKey = "testRoutingKey";

                /*
                 发送消息 basicPublish
                 * String exchange, 交换机
                 * String routingKey, 路由键
                 * BasicProperties props, 消息属性（需要单独声明）
                 * byte[] body，消息体
                 */
                AMQP.BasicProperties basicProperties = new AMQP.BasicProperties(
                        "application/octet-stream",
                        null,
                        null,
                        1,
                        0,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
                channel.basicPublish(exchangeName, routingKey, basicProperties, "hello".getBytes());

                /*
                BasicProperties的属性

                contentType	消息体的MIME类型，如application/json
                contentEncoding	消息的编码类型，如是否压缩
                headers	键/值对表，用户自定义任意的键和值
                deliveryMode 消息的持久化类型 ，1为非持久化，2为持久化，性能影响巨大
                priority 指定队列中消息的优先级
                correlationId 一般用作关联消息的message-id，常用于消息的响应
                replyTo	构建回复消息的私有响应队列
                expiration 消息的过期时刻，字符串，但是呈现格式为整型，精确到秒
                messageId 消息的唯一性标识，由应用进行设置
                timestamp 消息的创建时刻，整型，精确到秒
                type 消息类型名称，完全由应用决定如何使用该字段
                userId 标识已登录用户，极少使用
                appId 应用程序的类型和版本号
                clusterId 集群ID
                 */

            } catch (Exception e) {
                log.error("发送消息异常! ", e);
            } finally {
                try {
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

    static class MessageConsumer extends Thread {

        @Override
        public void run() {

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
                 * CancelCallback 当一个消费者取消订阅时的回调接口
                 * Consumer 消费者对象的回调接口

                 this.basicConsume(queue, autoAck, consumerTag, false, false, (Map)null, callback);
                 */

                Channel finalChannel = channel;
                System.out.println(channel);
                channel.basicConsume(queueName, autoAck, consumerTag, new DefaultConsumer(finalChannel) {

                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        System.out.println("路由键: " + envelope.getRoutingKey());
                        System.out.println("内容类型: " + properties.getContentType());
                        // false只确认当前一个消息收到, true确认所有 consumer 获得的消息（成功消费，消息从队列中删除）
                        finalChannel.basicAck(envelope.getDeliveryTag(), false);
                        System.out.println("消息体的内容: ");
                        System.out.println(new String(body, "UTF-8"));
                        System.out.println(finalChannel);
                        count.countDown();
                    }


                });

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
}

package com.hundsun.demo.java.mq.config;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq.config
 * @className: ConnectFactory
 * @description:
 * @author: h1123
 * @createDate: 2023/3/1 21:09
 */

public class ConnectFactory {

    private static final ConnectionFactory FACTORY;

    static {
        FACTORY = new ConnectionFactory();
        FACTORY.setUsername(MQConfig.RABBITMQ_USERNAME);
        FACTORY.setPassword(MQConfig.RABBITMQ_PASSWORD);
        // 地址
        FACTORY.setHost(MQConfig.RABBITMQ_HOST);
        FACTORY.setPort(MQConfig.RABBITMQ_PORT);
        FACTORY.setVirtualHost("/");

        init();
    }

    @SneakyThrows
    public static Connection getConnect() {
        return FACTORY.newConnection();
    }

    @SneakyThrows
    public static void init() {

        Connection connection = FACTORY.newConnection();
        Channel channel = connection.createChannel();

        /*
         声明交换机
         * String exchange, 交换机
         * String type, 交换机类型（direct，topic，fanout）
         * boolean durable, 是否持久化。代表交换机在服务器重启后是否还存在
         * boolean autoDelete, 自动删除
         * Map<String,Object> arguments，交换机其他属性，如x-message-ttl，x-max-length
         */
        channel.exchangeDeclare(MQConfig.EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true, false, null);

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
        channel.queueDeclare(MQConfig.QUEUE_NAME, true, false, false, null).getQueue();
        channel.queueBind(MQConfig.QUEUE_NAME, MQConfig.EXCHANGE_NAME, MQConfig.ROUTE_KEY);

        channel.close();
        connection.close();
    }
}

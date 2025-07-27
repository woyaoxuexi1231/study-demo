package org.hulei.springboot.rabbitmq.basic.config;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq.rabbit.config
 * @className: ConnectFactory
 * @description:
 * @author: h1123
 * @createDate: 2023/3/1 21:09
 */

public class ConnectFactory {

    private static final ConnectionFactory FACTORY;

    static {
        // 初始化连接工厂
        FACTORY = new ConnectionFactory();
        // 设置用户名密码
        FACTORY.setUsername(MQConfig.RABBITMQ_USERNAME);
        FACTORY.setPassword(MQConfig.RABBITMQ_PASSWORD);
        // 设置 ip 和 port
        FACTORY.setHost(MQConfig.RABBITMQ_HOST);
        FACTORY.setPort(MQConfig.RABBITMQ_PORT);
        // 设置虚拟机路径
        FACTORY.setVirtualHost("/");
        // 调用初始化方法, 这个方法放在 static 代码块中保证启动时只会运行一次
        // initQueue();
    }

    @SneakyThrows
    public static Connection getConnect() {
        return FACTORY.newConnection();
    }

    @SneakyThrows
    public static void producerInit() {
        initExchange();
    }

    @SneakyThrows
    public static void consumerInit() {
        initExchange();
        initQueue();
    }

    /**
     * 初始化队列
     */
    @SneakyThrows
    public static void initQueue() {

        // 建立代理服务器的连接
        Connection connection = FACTORY.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();

        // 声明队列, 这里执行这个方法之后就会创建相应的队列, 但是只是创建了队列
        channel.queueDeclare(MQConfig.TOPIC_MASTER_QUEUE, true, false, false, null);
        channel.queueDeclare(MQConfig.TOPIC_PULL_QUEUE, true, false, false, null);
        channel.queueDeclare(MQConfig.DIRECT_MASTER_QUEUE, true, false, false, null);

        // 这里在创建队列之后用 routing key 与 交换机进行绑定
        channel.queueBind(MQConfig.TOPIC_MASTER_QUEUE, MQConfig.TOPIC_EXCHANGE_NAME, MQConfig.TOPIC_MASTER_ROUTE_KEY);
        channel.queueBind(MQConfig.TOPIC_PULL_QUEUE, MQConfig.TOPIC_EXCHANGE_NAME, MQConfig.TOPIC_PULL_ROUTE_KEY);
        channel.queueBind(MQConfig.DIRECT_MASTER_QUEUE, MQConfig.DIRECT_EXCHANGE_NAME, MQConfig.DIRECT_MASTER_ROUTE_KEY);

        // 初始化工作完成, 关闭连接
        channel.close();
        connection.close();
    }

    /**
     * 初始化交换价
     */
    @SneakyThrows
    public static void initExchange() {

        // 建立代理服务器的连接
        Connection connection = FACTORY.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();

        /*
         声明交换机, 调用这个方法后会直接在 mq 上创建交换机, 按理说创建交换机的工作应该交给生产者, 而消费者只负责创建队列
         这里我的消费者测试类和生产者测试类都用了这个连接工厂, 没有分开处理了
         * String exchange, 交换机
         * String type, 交换机类型（direct，topic，fanout）
         * boolean durable, 是否持久化。代表交换机在服务器重启后是否还存在
         * boolean autoDelete, 自动删除
         * Map<String,Object> arguments，交换机其他属性，如x-message-ttl，x-max-length
         */
        channel.exchangeDeclare(MQConfig.TOPIC_EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true, false, null);

        channel.close();
        connection.close();
    }
}

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
        // 这里在创建队列之后用 routing key 与 交换机进行绑定
        channel.queueBind(MQConfig.TOPIC_MASTER_QUEUE, MQConfig.TOPIC_EXCHANGE_NAME, MQConfig.TOPIC_MASTER_ROUTE_KEY);
        channel.queueBind(MQConfig.TOPIC_PULL_QUEUE, MQConfig.TOPIC_EXCHANGE_NAME, MQConfig.TOPIC_PULL_ROUTE_KEY);


        // 配置当前队列的消息如果出现 1.被拒绝 2.过期 3.达到队列的最大消息数量 时应该投往死信队列的配置
        Map<String, Object> arguments = new HashMap<>();
        // 设置死信交换机, 这里设置的是, 如果这个队列内的消息出现问题, 消息应该往哪个死信队列发
        arguments.put("x-dead-letter-exchange", MQConfig.DEAD_EXCHANGE_NAME);
        // 设置死信 RoutingKey, 配置这个的作用时,当消息出现问题时,发往私信队列时应该以什么路由key去发送
        arguments.put("x-dead-letter-routing-key", MQConfig.TOPIC_FOR_DEAD_ROUTE_KEY);
        // 设置队列长度,这里作为测试死信队列,队列长度设置一个较短的值,当消息生产者发送的消息大于队列的最大能够容纳的消息数量时,超过的消息将会发往指定的死信队列
        arguments.put("x-max-length", 6);
        channel.queueDeclare(MQConfig.TOPIC_FOR_DEAD_QUEUE, true, false, false, arguments);
        channel.queueBind(MQConfig.TOPIC_FOR_DEAD_QUEUE, MQConfig.TOPIC_EXCHANGE_NAME, MQConfig.TOPIC_FOR_DEAD_QUEUE_ROUTE_KEY);
        // 上述的队列配置完成后,再配置一个消费死信交换机的队列,用于我们来消费发往死信交换机的消息
        channel.queueDeclare(MQConfig.DEAD_QUEUE_NAME, true, false, false, null);
        // 配置一个死信队列,绑定到上述的死信交换机,配置为刚才配置的死信路由来接收这个路由发过来的死信消息
        channel.queueBind(MQConfig.DEAD_QUEUE_NAME, MQConfig.DEAD_EXCHANGE_NAME, MQConfig.TOPIC_FOR_DEAD_ROUTE_KEY);


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
        channel.exchangeDeclare(MQConfig.DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, null);
        channel.close();
        connection.close();
    }
}

package com.hundsun.demo.java.mq.rabbit.config;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: MQConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/2/26 19:00
 */

public class MQConfig {

    /* RabbitMQ */
    public static final String RABBITMQ_USERNAME = "rabbitmq";
    public static final String RABBITMQ_PASSWORD = "rabbitmq";
    public static final String RABBITMQ_HOST = "192.168.80.128";
    public static final Integer RABBITMQ_PORT = 5672;

    /*
    Direct Exchange：直连交换机，根据Routing Key(路由键)进行投递到不同队列。
    Fanout Exchange：扇形交换机，采用广播模式，根据绑定的交换机，路由到与之对应的所有队列。
    Topic Exchange：主题交换机，对路由键进行模式匹配后进行投递，符号 # 表示一个或多个词，*表示一个词。
    Header Exchange：头交换机，不处理路由键。而是根据发送的消息内容中的 headers 属性进行匹配。
     */
    public static final String DIRECT_EXCHANGE_NAME = "direct-exchange";
    public static final String DIRECT_MASTER_ROUTE_KEY = "direct.route.key.master";
    public static final String DIRECT_MASTER_QUEUE = "direct-queue-master";
    public static final String DIRECT_SLAVE_ROUTE_KEY = "direct.route.key.slave";
    public static final String DIRECT_SLAVE_QUEUE = "direct-queue-slave";
    public static final String FANOUT_EXCHANGE_NAME = "fanout-exchange";
    public static final String FANOUT_MASTER_ROUTE_KEY = "fanout.route.key.master";
    public static final String FANOUT_MASTER_QUEUE = "fanout-queue-master";
    public static final String FANOUT_SLAVE_ROUTE_KEY = "fanout.route.key.slave";
    public static final String FANOUT_SLAVE_QUEUE = "fanout-queue-slave";
    public static final String TOPIC_EXCHANGE_NAME = "exchange-test-topic";
    public static final String TOPIC_MASTER_ROUTE_KEY = "topic.route.key.*";
    public static final String TOPIC_MASTER_QUEUE = "topic-queue-master";
    public static final String TOPIC_SLAVE_ROUTE_KEY = "topic.route.key.slave";
    public static final String TOPIC_SLAVE_QUEUE = "topic-queue-slave";
    public static final String HEADER_EXCHANGE_NAME = "header-exchange";
    public static final String HEADER_MASTER_ROUTE_KEY = "header.route.key.master";
    public static final String HEADER_MASTER_QUEUE = "header-queue-master";
    public static final String HEADER_SLAVE_ROUTE_KEY = "header.route.key.slave";
    public static final String HEADER_SLAVE_QUEUE = "header-queue-slave";


    /*
    死信队列
    1. 消息过期
    2. 达到队列最大长度
    3. 消息被拒
     */
    public static final String DEAD_EXCHANGE_NAME = "exchange-test-dead";
    public static final String DEAD_QUEUE_NAME = "dead-queue";


    public static String getExchangeName(String type) {
        switch (type) {
            case "fanout":
                return FANOUT_EXCHANGE_NAME;
            case "topic":
                return TOPIC_EXCHANGE_NAME;
            case "header":
                return HEADER_EXCHANGE_NAME;
            default:
                return DIRECT_EXCHANGE_NAME;
        }
    }
}

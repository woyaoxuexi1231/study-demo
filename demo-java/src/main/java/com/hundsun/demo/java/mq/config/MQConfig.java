package com.hundsun.demo.java.mq.config;

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
    public static final String RABBITMQ_HOST = "192.168.175.128";
    public static final Integer RABBITMQ_PORT = 5672;

    public static final String EXCHANGE_NAME = "hello-exchange";
    public static final String ROUTE_KEY = "testRoutingKey";
    public static final String QUEUE_NAME = "hello-consumer";
}

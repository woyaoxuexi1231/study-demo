package com.hundsun.demo.java.mq;

import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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

        // new MessageProducer(FACTORY,"1").start();
        // new MessageProducer(FACTORY,"2").start();
        log.info("消息发送完毕! ");
        // 确保消息先生产, 再被消费
        // Thread.sleep(1000);
        new MessageConsumer(FACTORY).start();
        // new MessageConsumer2(FACTORY).start();
    }

}

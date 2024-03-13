package com.hundsun.demo.springboot.rabbitmq.consumer;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.consumer.listener
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-03-12 16:06
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Configuration
public class RabbitConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory myContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);  // 设置并发消费者数量
        factory.setMaxConcurrentConsumers(10); // 设置最大并发消费者数量
        return factory;
    }
}

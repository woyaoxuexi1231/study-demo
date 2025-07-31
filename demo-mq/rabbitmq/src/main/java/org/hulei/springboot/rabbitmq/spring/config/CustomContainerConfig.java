package org.hulei.springboot.rabbitmq.spring.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hulei
 * @since 2025/7/27 21:58
 */

@Configuration
public class CustomContainerConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory myContainerFactory(ConnectionFactory connectionFactory) {
        /*
        SimpleRabbitListenerContainerFactory 主要用于创建和配置 SimpleMessageListenerContainer 实例
          - 容器工厂：批量生产配置一致的 RabbitMQ 消息监听容器
          - 统一配置：集中管理消息消费者的各种参数
          - 定制化：允许开发者根据需求定制消息监听行为
         */
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);  // 设置并发消费者数量,意味着spring内部会自动帮我们生成三个线程同时消费消息,有点类似线程池 coreWorker
        factory.setMaxConcurrentConsumers(10); // 设置最大并发消费者数量,随着消息积压,spring会自动帮我们增加消费者,直到达到设置的最大值, 类似线程池 maxWorker, 这个工作原理应该是spring判断长时间线程满载运行就增加线程
        factory.setPrefetchCount(1); // 设置为 1，表示每个线程每次只拉取一条消息
        return factory;
    }

}

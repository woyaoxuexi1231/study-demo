package org.hulei.springboot.rabbitmq.spring;

import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author hulei
 * @since 2024/9/23 21:06
 */

@Slf4j
@Configuration
public class RabbitAdminConfig {

    /**
     * rabbitAdmin
     * 1. 声明交换机
     * 2. 声明队列
     * 3. 声明绑定关系
     */
    @Autowired
    RabbitAdmin rabbitAdmin;

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @PostConstruct
    public void init() {
        exchangeDeclare();
        queueDeclare();
        bindingDeclare();
    }


    private void exchangeDeclare() {
        // 声明交换机
        TopicExchange exchange = new TopicExchange(
                MQConfig.TOPIC_EXCHANGE_NAME,
                true,
                false,
                null);
        rabbitAdmin.declareExchange(exchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", exchange);

        DirectExchange directExchange = new DirectExchange(
                MQConfig.DIRECT_EXCHANGE_NAME,
                true,
                false,
                null);
        rabbitAdmin.declareExchange(directExchange);
        log.info("使用 RabbitAdmin 创建交换机 {} 成功! ", directExchange);
    }

    private void queueDeclare() {
        Queue queue = new Queue(
                "rabbit_admin_queue",
                true,
                false,
                false,
                null
        );
        rabbitAdmin.declareQueue(queue);
        log.info("使用 RabbitAdmin 创建队列 {} 成功", queue);
    }

    private void bindingDeclare() {
        // 绑定队列和交换机的关系
        Binding binding = new Binding(
                "rabbit_admin_queue",
                Binding.DestinationType.QUEUE,
                MQConfig.TOPIC_EXCHANGE_NAME,
                "rabbit.admin.route.*",
                null
        );
        rabbitAdmin.declareBinding(binding);
        log.info("使用 RabbitAdmin 创建绑定关系 {} 成功", binding);

        // 还可以绑定交换机和交换机的关系
        Binding exchangeBinding = new Binding(
                MQConfig.DIRECT_EXCHANGE_NAME,
                Binding.DestinationType.EXCHANGE,
                MQConfig.TOPIC_EXCHANGE_NAME,
                MQConfig.TOPIC_TO_DIRECT_ROUTE_KEY,
                null
        );
        rabbitAdmin.declareBinding(exchangeBinding);
        log.info("使用 RabbitAdmin 创建绑定关系 {} 成功! ", exchangeBinding);

        Binding directQueue = new Binding(
                "direct-test",
                Binding.DestinationType.QUEUE,
                MQConfig.DIRECT_EXCHANGE_NAME,
                MQConfig.TOPIC_TO_DIRECT_ROUTE_KEY,
                null
        );
        rabbitAdmin.declareBinding(directQueue);
        log.info("使用 RabbitAdmin 创建绑定关系 {} 成功", directQueue);
    }

}

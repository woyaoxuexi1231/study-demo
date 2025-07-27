package org.hulei.springboot.rabbitmq.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author hulei
 * @since 2024/9/23 21:06
 */

@Slf4j
@Configuration
public class RabbitAdminConfig {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        /*
        RabbitAdmin 是 Spring AMQP（Spring 对 RabbitMQ 的集成框架）提供的一个管理工具类
          - 自动声明 Exchange、Queue 和 Binding；
          - 可以在应用启动时或运行时通过代码方式动态创建 RabbitMQ 的组件；
          - 是对 RabbitMQ 管理接口的封装，主要配合 RabbitTemplate 和 ConnectionFactory 使用。
         */
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

}

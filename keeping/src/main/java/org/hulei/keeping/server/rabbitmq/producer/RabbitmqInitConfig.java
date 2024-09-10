package org.hulei.keeping.server.rabbitmq.producer;

import com.hundsun.demo.commom.core.consts.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.dubbo.provider.config
 * @className: InitConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/3/3 0:01
 */
@Configuration
@Slf4j
public class RabbitmqInitConfig {

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
        // 声明交换机
        TopicExchange exchange = new TopicExchange(
                MQConfig.TOPIC_EXCHANGE_NAME,
                true,
                false,
                null);
        rabbitAdmin.declareExchange(exchange);
        log.info("创建交换机 {} 成功! ", exchange);
    }
}

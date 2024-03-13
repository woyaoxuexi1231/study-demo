package com.hundsun.demo.springboot.rabbitmq.producer;

import com.hundsun.demo.spring.mq.rabbit.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
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


    // @Bean
    // public Queue masterQueue() {
    //
    //     // 设置一些其他参数
    //     Map<String, Object> arguments = new HashMap<>();
    //     // 设置死信交换机
    //     arguments.put("x-dead-letter-exchange", MQConfig.DEAD_EXCHANGE_NAME);
    //     // 设置死信 RoutingKey
    //     arguments.put("x-dead-letter-routing-key", "");
    //     return new Queue(
    //             MQConfig.TOPIC_MASTER_QUEUE,
    //             true,
    //             false,
    //             false,
    //             arguments
    //     );
    // }
    //
    // @Bean
    // public Queue slaveQueue() {
    //
    //     // 设置一些其他参数
    //     Map<String, Object> arguments = new HashMap<>();
    //     // 设置死信交换机
    //     arguments.put("x-dead-letter-exchange", MQConfig.DEAD_EXCHANGE_NAME);
    //     // 设置死信 RoutingKey
    //     arguments.put("x-dead-letter-routing-key", "");
    //     Queue queue = new Queue(
    //             MQConfig.TOPIC_SLAVE_QUEUE,
    //             true,
    //             false,
    //             false,
    //             arguments
    //     );
    //     return queue;
    // }


    @Autowired
    RabbitAdmin rabbitAdmin;

    @PostConstruct
    public void init() {
        // 声明交换机
        rabbitAdmin.declareExchange(exchange());
        log.info("创建交换机 {} 成功! ", exchange());
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    public Exchange exchange() {
        return new TopicExchange(
                MQConfig.TOPIC_EXCHANGE_NAME,
                true,
                false,
                null);
    }
}

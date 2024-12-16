package org.hulei.springboot.rabbitmq.spring.consumer;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author h1123
 * @since 2023/3/11 0:28
 */

@Configuration
@Component
@Slf4j
public class TopicExchangeListener {

    /*============================================== 配置 ===========================================*/

    @Bean
    public SimpleRabbitListenerContainerFactory myContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);  // 设置并发消费者数量,意味着spring内部会自动帮我们生成三个线程同时消费消息,有点类似线程池 coreWorker
        factory.setMaxConcurrentConsumers(10); // 设置最大并发消费者数量,随着消息积压,spring会自动帮我们增加消费者,直到达到设置的最大值, 类似线程池 maxWorker, 这个工作原理应该是spring判断长时间线程满载运行就增加线程
        factory.setPrefetchCount(1); // 设置为 1，表示每个线程每次只拉取一条消息
        return factory;
    }

    /**
     * 如果你没有配置自定义的 SimpleRabbitListenerContainerFactory，Spring AMQP 会使用默认的 RabbitListenerContainerFactory 实现(SimpleRabbitListenerContainerFactory 的一个实例)
     * 默认的工厂配置旨在提供一套合理的默认行为，适用于大多数简单场景。
     * RabbitAnnotationDrivenConfiguration会默认配置一个 SimpleRabbitListenerContainerFactory
     * <p>
     * 指定了containerFactory之后, 会使用指定的containerFactory来注册这个监听器, 不会再使用springboot默认的监听器容器来注册了
     * <p>
     * author: hulei42031
     * date: 2024-03-12 18:09
     *
     * @param message message
     * @param channel channel
     */
    @RabbitListener(
            containerFactory = "myContainerFactory",
            bindings = @QueueBinding(
                    value = @Queue(
                            name = "custom_container_queue",
                            exclusive = "false",
                            autoDelete = "false",
                            durable = "true"
                    ),
                    exchange = @Exchange(
                            value = MQConfig.TOPIC_EXCHANGE_NAME,
                            type = ExchangeTypes.TOPIC
                    ),
                    key = MQConfig.KEY_FOR_CUSTOM_CONTAINER
            )
    )
    @SneakyThrows
    public void handleWithCustomContainer(String message, Channel channel) {
        log.info("收到消息, 暂停 5 秒消费. msg: {}", message);
        // 自定义容器并发消费者是3,所以同一时间,会有三个线程收到消息处理并在这里阻塞,如果队列内的消息超过三条,那么对于这个进程来说将不会再接收消息
        Thread.sleep(5000);
    }


    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = MQConfig.TOPIC_MASTER_QUEUE, // 队列的名称。如果留空，将创建一个具有随机名称的队列，通常用于声明临时队列。
                            durable = "true", // 表示队列是否持久化。持久化的队列会在 RabbitMQ 重启后依然存在，默认值是 false。
                            autoDelete = "false", // 表示队列是否自动删除。当最后一个消费者断开连接之后队列是否自动删除，默认值是 false。
                            exclusive = "false" // 表示队列是否是排他的。排他队列只对首次声明它的连接可见，并在连接关闭时自动删除，默认值是 false。
                    ),
                    exchange = @Exchange(value = MQConfig.TOPIC_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                    key = MQConfig.TOPIC_MASTER_ROUTE_KEY // 用于定义绑定键（routing key），这是一个字符串，决定了消息如何路由到队列。
            )
    )
    public void receiveMasterMsg(Message msg, Channel channel) {
        log.info("{} 收到消息: {}", msg.getMessageProperties().getReceivedRoutingKey(), JSON.parseObject(msg.getBody(), String.class));
        try {
            // Thread.sleep(5000);
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("ack异常", e);
        }
    }
}

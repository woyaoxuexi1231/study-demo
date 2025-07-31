package org.hulei.springboot.rabbitmq.spring.consumer;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2025/7/27 21:58
 */

@Slf4j
@Component
public class CustomContainerListener {


    /**
     * RabbitListener 注解会自动生成队列，交换机，以及映射关系
     *
     * @param message message
     * @param channel channel
     */
    @RabbitListener(
            /*
            如果你没有配置自定义的 SimpleRabbitListenerContainerFactory，Spring AMQP 会使用默认的 RabbitListenerContainerFactory 实现(SimpleRabbitListenerContainerFactory 的一个实例)
            默认的工厂配置旨在提供一套合理的默认行为，适用于大多数简单场景。
            RabbitAnnotationDrivenConfiguration 指定了containerFactory之后, 会使用指定的containerFactory来注册这个监听器, 不会再使用springboot默认的监听器容器来注册了
             */
            containerFactory = "myContainerFactory",
            bindings = @QueueBinding(
                    value = @Queue(
                            name = "custom_container_queue",
                            exclusive = "false",
                            autoDelete = "false",
                            durable = "true"
                    ),
                    exchange = @Exchange(
                            value = MQConfig.NORMAL_TOPIC_EXCHANGE,
                            type = ExchangeTypes.TOPIC
                    ),
                    key = "key.for.custom.container"
            )
    )
    @SneakyThrows
    public void handleWithCustomContainer(String message, Channel channel) {
        log.info("收到消息, 暂停 5 秒消费. msg: {}", message);
        // 自定义容器并发消费者是3,所以同一时间,会有三个线程收到消息处理并在这里阻塞,如果队列内的消息超过三条,那么对于这个进程来说将不会再接收消息
        Thread.sleep(5000);
    }


}

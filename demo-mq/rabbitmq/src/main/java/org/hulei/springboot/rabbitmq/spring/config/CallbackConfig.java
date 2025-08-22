package org.hulei.springboot.rabbitmq.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * 发送消息的回调类,用于消息可靠性
 * ① 实现了 ConfirmCallback 和 ReturnsCallback
 * ② 类完成了自我注册
 */

@Slf4j
@Component
public class CallbackConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    /**
     * 注入容器里的rabbitTemplate, 以便对容器里的rabbitTemplate进行改造
     */
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 是 Spring AMQP 中的一个连接工厂类，用于管理 RabbitMQ 连接。它是 Spring AMQP 提供的一个抽象，用于简化 RabbitMQ 的连接和资源管理。
     * 这里注入, 可以用于配置 spring.rabbitmq.publisher-confirm-type 和 spring.rabbitmq.publisher-returns 这两个参数(可以, 但没必要这样搞.直接写配置就行)
     */
    @Autowired
    CachingConnectionFactory cachingConnectionFactory;

    /**
     * 给 rabbitTemplate 注册这个回调方法
     */
    @PostConstruct
    public void bind() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }


    /**
     * 1. 交换机成功收到了消息会触发这个回调(不管有没有队列收到消息, 都会触发, ack = true)
     * 2. 交换机接收失败了也会触发这个回调
     *
     * @param correlationData 消息相关数据，即使用 CorrelationData 关联的数据，可以用于标识消息或追踪消息的处理。
     * @param ack             表示消息是否被正确地发送到交换机，true 表示发送成功，false 表示发送失败。
     * @param cause           如果消息发送失败，该字段包含一个描述失败原因的字符串；如果消息发送成功，该字段为 null。
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        /*
        实现 ConfirmCallback 时，需要实现这个方法
         */


        if (ack) {
            log.info("交换机成功收到了消息(但是交换机并不保证消息被正常路由到某个队列), correlationData: {}", correlationData);
        } else {
            /*
            1. Broker 内存或磁盘资源不足
            2. 交换机不存在

            这里如果需要做补偿，那么应该在 correlationData 配置唯一标识，然后消息应该需要在某个地方做持久化，通过唯一标识找到消息再进行后续操作
             */
            log.error("confirm 函数收到通知, 交换机没能收到消息, correlationData: {}, 失败的原因是: {}", correlationData, cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        /*
        实现 ReturnsCallback 接口时需要实现此接口
        开启方式(2选1即可)：
            spring.rabbitmq.publisher-returns=true
            rabbitTemplate.setMandatory(true);

        ReturnsCallback 用于处理无法路由到任何队列的消息（即消息被返回给生产者的情况）。
        工作原理
            1.当消息无法从交换器路由到任何队列时（没有匹配的绑定）
            2.如果设置了 mandatory 标志为 true，RabbitMQ 会将消息返回给生产者
            3.通过实现 ReturnsCallback 可以处理这些被返回的消息

        💡对于延迟队列的消息，也会触发这个回调
         */
        log.info("returned 函数收到通知, 消息并没有发送到任何有效的队列中, returned: {}", returned);
    }
}

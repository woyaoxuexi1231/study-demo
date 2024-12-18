package org.hulei.springboot.rabbitmq.spring.producer;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 发送消息的回调类,用于消息可靠性
 * ① 实现了 ConfirmCallback 和 ReturnsCallback
 * ② 类完成了自我注册
 */

@Slf4j
@Component
public class Callback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

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
        /*
        ConfirmCallback 是 RabbitMQ 在消息发送过程中的确认回调接口，用于确认消息是否成功发送到交换机。
        当消息成功发送到交换机时，RabbitMQ 会触发 ConfirmCallback 回调方法来通知发送者。
        通过实现 ConfirmCallback 接口，可以在回调方法中处理发送成功和失败的情况，以便根据需要进行相应的处理逻辑。

        在实际使用中，可以通过设置 ConfirmCallback 回调来处理以下情况：
            监测消息发送成功/失败：根据 ack 的值来判断消息是否成功发送到交换机，根据 correlationData 来判断是哪条消息发送成功或失败。
            数据库操作的事务性：当消息发送成功后，再执行一些数据库操作，如记录消息的状态或更新相关数据，以保证消息的可靠性。
            重发机制：当消息发送失败时，可以根据需要执行一些重发逻辑，如重新发送消息或进行异常处理。

        需要使用这个参数使其生效
        spring.rabbitmq.publisher-confirm-type=correlated
         */
        rabbitTemplate.setConfirmCallback(this);
        /*
        ReturnCallback 是 RabbitMQ 在无法将消息路由到队列时的返回回调接口。当消息无法路由到队列时，RabbitMQ 会触发 ReturnCallback 回调方法，通知发送者该消息的返回情况。
        通常情况下，消息无法路由到队列的原因可能是因为交换机没有匹配的队列，或者消息被标记为 “mandatory” 而没有被路由到任何队列。
        通过实现 ReturnCallback 接口，可以在回调方法中处理这些返回的消息，以便根据需要进行相应的处理逻辑。

        通过 ReturnCallback 可以在消息无法路由到队列时处理一些后续的操作，如记录日志、发送告警、重新发送消息等。可以根据 replyCode 和 replyText 处理特定的错误情况，以实现更高级的错误处理逻辑。
        需要注意的是，为了触发 ReturnCallback，需要在消息发送时设置 mandatory 参数为 true，否则即使消息无法路由到队列，也不会触发该回调。
        通过 ReturnCallback 可以对发送到交换机但无法路由到队列的消息进行处理，确保消息的可靠性和处理返回的错误情况。

        使用这两个都可以开启
        spring.rabbitmq.publisher-returns=true
        rabbitTemplate.setMandatory(true);
         */
        // rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }


    /**
     * 1. 交换机成功收到了消息会触发这个回调(不管有没有队列收到消息, 都会触发)
     * 2. 交换机接收失败了也会触发这个回调
     *
     * @param correlationData 消息相关数据，即使用 CorrelationData 关联的数据，可以用于标识消息或追踪消息的处理。
     * @param ack             表示消息是否被正确地发送到交换机，true 表示发送成功，false 表示发送失败。
     * @param cause           如果消息发送失败，该字段包含一个描述失败原因的字符串；如果消息发送成功，该字段为 null。
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // ack = true 交换机收到了消息, ack = false 交换机没收到消息
        // 我这里在项目启动之后直接把交换机删掉了,来模拟消息到不了交换机的场景
        if (ack) {
            log.info("回调函数收到通知, 交换机成功收到了消息, correlationData: {}", correlationData);
        } else {
            log.error("回调函数收到通知, 交换机没能收到消息, correlationData: {}, 失败的原因是: {}", correlationData, cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("回调函数收到通知,消息并没有发送到任何有效的队列中, returned: {}", returned);
    }
}

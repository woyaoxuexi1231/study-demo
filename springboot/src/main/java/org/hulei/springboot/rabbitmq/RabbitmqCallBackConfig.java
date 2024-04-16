package org.hulei.springboot.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.dubbo.provider.callback
 * @className: MyConfirmCallBack
 * @description:
 * @author: h1123
 * @createDate: 2023/3/19 17:22
 */
@Slf4j
@Component
public class RabbitmqCallBackConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

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
        rabbitTemplate.setReturnCallback(this);
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
        // log.info("confirm => correlationData: {}, ack: {}, cause: {}", correlationData, ack, cause);
    }


    /**
     * 在消息送不到相应队列的时候会触发这个回调
     * 1. 发送了错误得routingKey导致没有队列能够收到这个消息, 即使交换机收到了消息, 也会触发这个回调函数
     *
     * @param message    the returned message. 返回的消息内容。
     * @param replyCode  the reply code. 返回的响应码。
     * @param replyText  the reply text. 返回的响应文本。
     * @param exchange   the exchange. 消息发送时指定的交换机。
     * @param routingKey the routing key. 消息发送时指定的路由键。
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息并没有成功到达任何队列, message: {}, replyCode: {}, replyText: {}, exchange: {}, routingKey: {}", message, replyCode, replyText, exchange, routingKey);
    }
}

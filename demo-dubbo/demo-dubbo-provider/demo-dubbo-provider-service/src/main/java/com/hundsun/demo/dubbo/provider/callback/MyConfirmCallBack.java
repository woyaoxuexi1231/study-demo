package com.hundsun.demo.dubbo.provider.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
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
public class MyConfirmCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {


    /**
     * 1. 交换机成功收到了消息会触发这个回调
     * 2. 交换机接收失败了也会触发这个回调
     *
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // ack = true 交换机收到了消息, ack = false 交换机没收到消息
        if (ack) {
            log.info("交换机成功收到了消息! 消息为: {}", correlationData.getId());
        } else {
            log.info("交换机接收消息失败了! 消息为: {}, 原因为: {}", correlationData.getId(), cause);
        }
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 给 rabbitTemplate 注册这个回调方法
     */
    @PostConstruct
    public void bind() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 在消息送不到相应队列的时候会触发这个回调, 例如 routingKey 错了
     *
     * @param message    the returned message.
     * @param replyCode  the reply code.
     * @param replyText  the reply text.
     * @param exchange   the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("传递失败的消息为: {}, replyCode: {}, 退回原因: {}, 交换机: {}, routingKey: {}", message, replyCode, replyText, exchange, routingKey);
    }
}

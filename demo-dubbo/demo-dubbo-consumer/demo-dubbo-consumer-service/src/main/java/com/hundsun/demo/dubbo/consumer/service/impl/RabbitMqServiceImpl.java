package com.hundsun.demo.dubbo.consumer.service.impl;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.dubbo.consumer.api.service.RabbitMqService;
import com.hundsun.demo.java.mq.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.consumer.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-17 13:49
 */
@Slf4j
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResultDTO<?> sentSampleMsg() {

        try {
            String msg = "hello rabbitmq!";
            MessagePostProcessor messagePostProcessor = message -> {
                // 设置消息的过期时间
                message.getMessageProperties().setExpiration("30000");
                return message;
            };
            /*
            convertSendAndReceive(…) - 可以同步消费者。使用此方法, 当确认了所有的消费者都接收成功之后, 才触发另一个 convertSendAndReceive(…) 也就是才会接收下一条消息。RPC调用方式。
                exchange – the exchange.
                routingKey – the routing key.
                message – the data to send.
                messagePostProcessor – a message post processor (can be null).
                correlationData – correlation data (can be null).
            convertAndSend(…) - 使用此方法, 交换机会马上把所有的信息都交给所有的消费者, 消费者再自行处理, 不会因为消费者处理慢而阻塞线程。
             */
            rabbitTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE_NAME, (Object) msg, messagePostProcessor);
        } catch (Exception e) {
            log.error("消息发送异常!", e);
            return ResultDTOBuild.resultErrorBuild("消息发送异常!");
        }
        return ResultDTOBuild.resultDefaultBuild();
    }

}

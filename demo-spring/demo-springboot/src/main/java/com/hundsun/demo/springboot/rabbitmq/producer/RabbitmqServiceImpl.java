package com.hundsun.demo.springboot.rabbitmq.producer;

import com.alibaba.fastjson.JSON;
import com.hundsun.demo.commom.core.model.MQIdempotency;
import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.spring.mq.rabbit.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
public class RabbitmqServiceImpl implements RabbitmqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResultDTO<?> sentSampleMsg() {
        try {

            // 创建一个消息实体
            MQIdempotency idempotency = new MQIdempotency();
            String uuid = UUID.randomUUID().toString();
            // String uuid = "2";
            idempotency.setUuid(uuid);
            idempotency.setMsg("hello rabbitmq!");

            // 用于在将消息发送到消息队列之前对消息进行后处理。它允许你在消息发送前对消息的各个属性进行修改或者添加一些额外的处理逻辑。
            MessagePostProcessor messagePostProcessor = message -> {
                // 设置消息的过期时间
                // message.getMessageProperties().setExpiration("30000");
                return message;
            };

            // 用于跟踪消息发送和接收的关联数据的类, 在消息发送成功或失败时，Spring AMQP 将使用该对象来跟踪消息的状态和结果。可以不设置
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(uuid);

            this.pushAsyncConfirm(idempotency, messagePostProcessor, correlationData);
            // this.invoke(idempotency, messagePostProcessor, correlationData);

        } catch (Exception e) {
            log.error("sentSampleMsg error!", e);
            return ResultDTOBuild.resultErrorBuild("sentSampleMsg error!");
        }
        return ResultDTOBuild.resultDefaultBuild();
    }

    private void pushSyncConfirm(MQIdempotency message, MessagePostProcessor messagePostProcessor, CorrelationData correlationData) {
        rabbitTemplate.invoke(operations -> {
            rabbitTemplate.convertAndSend(
                    MQConfig.TOPIC_EXCHANGE_NAME, // the exchange. 如果交换机不存在,reply-code=404, reply-text=NOT_FOUND - no exchange 'exchange-test-topic2' in vhost '/', class-id=60, method-id=40
                    MQConfig.TOPIC_MASTER_ROUTE_KEY, // the routing key.
                    JSON.toJSONString(message), // the data to send.
                    messagePostProcessor, // a message post processor (can be null).
                    correlationData // correlation data (can be null).
            );
            try {
                // 阻塞式发布确认, 如果配置了confirm回调, 依旧会触发confirm回调
                boolean confirms = rabbitTemplate.waitForConfirms(1000); // 等待1秒
                log.info("rabbitTemplate.waitForConfirms => {}", confirms);
                return confirms;
            } catch (AmqpException e) {
                // 处理异常情况
                return false;
            }
        });
    }

    private void pushAsyncConfirm(MQIdempotency message, MessagePostProcessor messagePostProcessor, CorrelationData correlationData) {
        // 使用此方法, 交换机会马上把所有的信息都交给所有的消费者, 消费者再自行处理, 不会因为消费者处理慢而阻塞线程。
        rabbitTemplate.convertAndSend(
                MQConfig.TOPIC_EXCHANGE_NAME, // the exchange.
                MQConfig.TOPIC_MASTER_ROUTE_KEY, // the routing key.
                JSON.toJSONString(message), // the data to send.
                messagePostProcessor, // a message post processor (can be null).
                correlationData // correlation data (can be null).
        );
        // 可以同步消费者。使用此方法, 当确认了所有的消费者都接收成功之后, 才触发另一个 convertSendAndReceive(…) 也就是才会接收下一条消息。RPC调用方式。
        // rabbitTemplate.convertSendAndReceive()
    }

}

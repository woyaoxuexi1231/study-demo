package org.hulei.springboot.rabbitmq.spring.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.hulei.entity.mybatisplus.domain.MQIdempotency;
import org.hulei.springboot.rabbitmq.basic.config.MQConfig;
import org.hulei.util.dto.ResultDTO;
import org.hulei.util.utils.ResultDTOBuild;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.rabbitmq.producer
 * @className: RabbitmqController
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/15 23:57
 */

@Slf4j
@RestController
@RequestMapping(value = "/rabbimq")
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @GetMapping(value = "/sentMasterTopicMsg")
    public ResultDTO<?> sentMasterTopicMsg() {
        try {

            // 创建一个消息实体
            MQIdempotency idempotency = new MQIdempotency();
            String uuid = UUID.randomUUID().toString();
            // String uuid = "2";
            idempotency.setUuid(uuid);
            idempotency.setMsg("hello rabbitmq!");
            // 用于跟踪消息发送和接收的关联数据的类, 在消息发送成功或失败时，Spring AMQP 将使用该对象来跟踪消息的状态和结果。可以不设置
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(uuid);

            // 使用invoke包裹执行,内部的逻辑为同步执行,再通过waitForConfirms方法来支持同步的消息确认,保证消息的可靠性
            rabbitTemplate.invoke(operations -> {
                rabbitTemplate.convertAndSend(
                        MQConfig.TOPIC_EXCHANGE_NAME, // the exchange. 如果交换机不存在,reply-code=404, reply-text=NOT_FOUND - no exchange 'exchange-test-topic2' in vhost '/', class-id=60, method-id=40
                        MQConfig.TOPIC_MASTER_ROUTE_KEY, // the routing key.
                        JSON.toJSONString(correlationData), // the data to send.
                        message -> message, // a message post processor (can be null).
                        correlationData // correlation data (can be null).
                );
                try {
                    // 阻塞式发布确认, 如果配置了confirm回调, 依旧会触发confirm回调
                    boolean confirms = rabbitTemplate.waitForConfirms(1000); // 等待1秒
                    log.info("等待rabbitMQ确认完成,结果: {}", confirms);
                    return confirms;
                } catch (AmqpException e) {
                    // 处理异常情况
                    log.error("等待rabbitMQ确认出现异常, ", e);
                    return false;
                }
            });
            // 可以同步消费者。使用此方法, 当确认了所有的消费者都接收成功之后, 才触发另一个 convertSendAndReceive(…) 也就是才会接收下一条消息。RPC调用方式。
            // rabbitTemplate.convertSendAndReceive()

        } catch (Exception e) {
            log.error("sentSampleMsg error!", e);
            return ResultDTOBuild.resultErrorBuild("sentSampleMsg error!");
        }
        return ResultDTOBuild.resultDefaultBuild();
    }

    @GetMapping("/sendTopicForDeadMsg")
    public void sendTopicForDeadMsg() {
        // 用于在将消息发送到消息队列之前对消息进行后处理。它允许你在消息发送前对消息的各个属性进行修改或者添加一些额外的处理逻辑。
        MessagePostProcessor messagePostProcessor = message -> {
            // 设置消息的过期时间
            message.getMessageProperties().setExpiration("30000");
            return message;
        };
        // convertAndSend方法本身是同步的,但是他仅仅只是把消息刷入连接的发送缓冲区,后续就直接返回了,所以并不提供发布确认机制
        rabbitTemplate.convertAndSend(
                MQConfig.TOPIC_EXCHANGE_NAME,
                MQConfig.TOPIC_FOR_DEAD_QUEUE_ROUTE_KEY,
                "hello world",
                messagePostProcessor
        );
    }

    @GetMapping("/sendMsgToCustomContainerQueue")
    public void sendMsgToCustomContainerQueue() {
        // 每次产生500条消息
        for (int i = 0; i < 500; i++) {
            rabbitTemplate.convertAndSend(
                    MQConfig.TOPIC_EXCHANGE_NAME,
                    MQConfig.KEY_FOR_CUSTOM_CONTAINER,
                    "hello custom container"
            );
        }
    }

    @GetMapping("/topicExchangeToDirectExchange")
    public void topicExchangeToDirectExchange() {
        rabbitTemplate.convertAndSend(
                MQConfig.TOPIC_EXCHANGE_NAME,
                MQConfig.TOPIC_TO_DIRECT_ROUTE_KEY,
                "hello direct"
        );
    }


}

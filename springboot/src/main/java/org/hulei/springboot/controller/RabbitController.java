package org.hulei.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.hundsun.demo.commom.core.model.MQIdempotency;
import com.hundsun.demo.spring.mq.rabbit.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @projectName: study-demo
 * @package: org.hulei.springboot.controller
 * @className: RabbitController
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/10 23:34
 */

@Slf4j
@RestController
@RequestMapping(value = "/rabbit")
public class RabbitController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping(value = "/sentSampleMsg")
    public void sentSampleMsg(@RequestParam(value = "uuid", required = false) String uuid) {

        try {

            MQIdempotency idempotency = new MQIdempotency();
            uuid = uuid == null ? UUID.randomUUID().toString() : uuid;

            log.info("开始发送消息! uuid: {}", uuid);
            idempotency.setUuid(uuid);
            idempotency.setMsg("hello rabbitmq!");


            MessagePostProcessor messagePostProcessor = message -> {
                // 设置消息的过期时间
                // message.getMessageProperties().setExpiration("30000");
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
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(uuid);
            rabbitTemplate.convertAndSend(
                    MQConfig.TOPIC_EXCHANGE_NAME,
                    MQConfig.TOPIC_MASTER_ROUTE_KEY,
                    JSON.toJSONString(idempotency),
                    messagePostProcessor,
                    correlationData);
            // rabbitTemplate.convertSendAndReceive()
        } catch (Exception e) {
            log.error("消息发送异常!", e);
        }
    }

}

package com.hundsun.demo.dubbo.consumer.service.impl;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.dubbo.consumer.service.RabbitMqService;
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
            String routingKey = "simple_helloMsg";
            String msg = "hello rabbitmq!";
            MessagePostProcessor messagePostProcessor = message -> {
                // 设置消息的过期时间
                message.getMessageProperties().setExpiration("30000");
                return message;
            };
            rabbitTemplate.convertAndSend(routingKey, (Object) msg, messagePostProcessor);
        } catch (Exception e) {
            log.error("消息发送异常!", e);
            return ResultDTOBuild.resultErrorBuild("消息发送异常!");
        }
        return ResultDTOBuild.resultDefaultBuild();
    }

}

package com.hundsun.demo.dubbo.consumer.service.impl;

import com.hundsun.demo.dubbo.common.api.model.dto.ResultDTO;
import com.hundsun.demo.dubbo.common.api.utils.ResultDTOBuild;
import com.hundsun.demo.dubbo.consumer.service.RabbitMqService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
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
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Slf4j
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResultDTO sentSampleMsg() {

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration("30000");
        String msg = "hello";
        log.info("sent the msg 'hello'");
        rabbitTemplate.convertAndSend("notice_queue",msg,messageProperties);
        return ResultDTOBuild.resultDefaultBuild();
    }
    
}

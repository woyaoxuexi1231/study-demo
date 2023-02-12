package com.hundsun.demo.dubbo.consumer.service;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.consumer.service
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-17 13:48
 */

public interface RabbitMqService {

    ResultDTO sentSampleMsg();
}

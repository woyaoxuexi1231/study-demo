package com.hundsun.demo.dubbo.consumer.service;

import com.hundsun.demo.dubbo.common.api.model.dto.ResultDTO;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.consumer.service
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-17 13:48
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public interface RabbitMqService {

    ResultDTO sentSampleMsg();
}

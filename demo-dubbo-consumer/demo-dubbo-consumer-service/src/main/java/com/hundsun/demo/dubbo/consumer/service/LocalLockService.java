package com.hundsun.demo.dubbo.consumer.service;

import com.hundsun.demo.dubbo.common.api.model.dto.ResultDTO;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.consumer.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-02 10:23
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public interface LocalLockService {

    /**
     * 操作共享资源
     */
    void decreaseSharedResource();

    /**
     * 获取共享资源
     *
     * @return
     */
    ResultDTO getSharedResource();
}

package com.hundsun.demo.dubbo.consumer.service;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.consumer.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-02 10:23
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

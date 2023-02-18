package com.hundsun.demo.dubbo.provider.api.service;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.amust.dubbodemo.service
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:14
 */

public interface SimpleProviderService {

    /**
     * simple rpc invoke
     *
     * @return ?
     */
    ResultDTO<?> RpcSimpleInvoke();
}

package com.hundsun.demo.dubbo.consumer.api.service;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import org.springframework.validation.annotation.Validated;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.dubbodemo.consumer.service
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:21
 */
@Validated
public interface SimpleService {

    /**
     * simple rpc invoke
     *
     * @return ?
     */
    ResultDTO<?> simpleRpcInvoke();
}

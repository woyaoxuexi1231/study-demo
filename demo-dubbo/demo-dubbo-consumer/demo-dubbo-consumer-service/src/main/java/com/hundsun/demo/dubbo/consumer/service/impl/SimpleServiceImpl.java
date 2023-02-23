package com.hundsun.demo.dubbo.consumer.service.impl;

import com.hundsun.demo.commom.core.annotation.DoneTime;
import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.dubbo.consumer.api.service.SimpleService;
import com.hundsun.demo.dubbo.consumer.api.service.LocalLockService;
import com.hundsun.demo.dubbo.provider.api.service.SimpleProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.dubbodemo.consumer.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:24
 */
@Slf4j
@Component
public class SimpleServiceImpl implements SimpleService {

    /**
     * 微服务
     */
    @DubboReference(check = false, timeout = 30000, loadbalance = RandomLoadBalance.NAME)
    SimpleProviderService simpleProviderService;

    /**
     * redis
     */
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    /**
     * local lock
     */
    @Autowired
    LocalLockService localLockService;

    @Override
    @DoneTime
    public ResultDTO<?> simpleRpcInvoke() {
        return simpleProviderService.RpcSimpleInvoke();
    }
}



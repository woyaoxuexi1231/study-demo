package com.hundsun.demo.dubbo.consumer.service.impl;

import com.hundsun.demo.dubbo.common.api.annotation.DoneTime;
import com.hundsun.demo.dubbo.common.api.model.dto.ResultDTO;
import com.hundsun.demo.dubbo.common.api.utils.ResultDTOBuild;
import com.hundsun.demo.dubbo.consumer.api.service.SimpleService;
import com.hundsun.demo.dubbo.consumer.service.LocalLockService;
import com.hundsun.demo.dubbo.provider.api.model.request.UserRequestDTO;
import com.hundsun.demo.dubbo.provider.api.service.SimpleProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Slf4j
@Component
public class SimpleServiceImpl implements SimpleService {

    /**
     * 微服务
     */
    @DubboReference(check = false)
    SimpleProviderService simpleProviderService;

    /**
     * redis
     */
    @Autowired
    RedisTemplate<String, Object> redisTemplateSO;

    /**
     * local lock
     */
    @Autowired
    LocalLockService localLockService;

    // @Autowired
    // RedissonClient redissonClient;


    @Override
    @DoneTime
    public ResultDTO getHelloWorld(String hello) {
        return simpleProviderService.getHelloWorld(hello);
    }

    @Override
    public ResultDTO addUser(UserRequestDTO userRequestDTO) {
        return simpleProviderService.insertUser(userRequestDTO);
    }

    @Override
    public ResultDTO addRedisInfo(UserRequestDTO req) {
        redisTemplateSO.opsForSet().add("userInfo", req);
        return ResultDTOBuild.resultSuccessBuild(redisTemplateSO.opsForSet().members("userInfo"));
    }

    @Override
    public ResultDTO testLock() {
        for (int i = 0; i < 10; i++) {
            localLockService.decreaseSharedResource();
        }
        return ResultDTOBuild.resultSuccessBuild(localLockService.getSharedResource());
    }

    @Override
    public ResultDTO redisson() {
        // RLock rLock = redissonClient.getLock("myLock");
        // rLock.lock();
        // try {
        //     Thread.sleep(5000);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
        // rLock.unlock();
        return ResultDTOBuild.resultDefaultBuild();
    }

}



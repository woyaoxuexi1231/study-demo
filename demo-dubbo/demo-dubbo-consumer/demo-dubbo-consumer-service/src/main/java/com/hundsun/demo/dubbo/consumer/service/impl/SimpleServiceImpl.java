package com.hundsun.demo.dubbo.consumer.service.impl;

import com.hundsun.demo.commom.core.annotation.DoneTime;
import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.dubbo.consumer.api.service.SimpleService;
import com.hundsun.demo.dubbo.consumer.service.LocalLockService;
import com.hundsun.demo.dubbo.provider.api.model.request.UserRequestDTO;
import com.hundsun.demo.dubbo.provider.api.model.request.UserSelectReqDTO;
import com.hundsun.demo.dubbo.provider.api.service.SimpleProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    // @Autowired
    // RedissonClient redissonClient;


    @Override
    @DoneTime
    public ResultDTO getHelloWorld(String hello) {
        return simpleProviderService.getHelloWorld(hello);
    }

    @Override
    public ResultDTO<?> addUser(UserRequestDTO req) {
        // 先插入mysql, 再插入redis
        simpleProviderService.insertUser(req);
        redisTemplate.opsForSet().add("userInfo", req);
        return ResultDTOBuild.resultDefaultBuild();
    }

    @Override
    public ResultDTO addRedisInfo(UserRequestDTO req) {
        redisTemplate.opsForSet().add("userInfo", req);
        return ResultDTOBuild.resultSuccessBuild(redisTemplate.opsForSet().members("userInfo"));
    }

    @Override
    public ResultDTO<?> selectUser(UserSelectReqDTO req) {

        if (StringUtils.isBlank(req.getName())) {
            return ResultDTOBuild.resultDefaultBuild();
        }

        List<UserRequestDTO> rsp = new ArrayList<>();

        // 先查redis再查mysql
        Set<?> redisUsers = redisTemplate.opsForSet().members("userInfo");
        for (Object redisUser : redisUsers) {
            UserRequestDTO requestDTO = (UserRequestDTO) redisUser;
            if (requestDTO.getName().contains(req.getName())) {
                rsp.add(requestDTO);
            }
        }

        if (!rsp.isEmpty()) {
            return ResultDTOBuild.resultSuccessBuild(rsp);
        }

        return ResultDTOBuild.resultSuccessBuild(simpleProviderService.selectUser(req).getData());
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



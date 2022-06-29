package com.hundsun.demo.dubbo.consumer.api.service;

import com.hundsun.demo.dubbo.common.api.model.dto.ResultDTO;
import com.hundsun.demo.dubbo.provider.api.model.request.UserRequestDTO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.dubbodemo.consumer.service
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:21
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Validated
public interface SimpleService {

    /**
     * 从服务端获取一个简单的字符串
     *
     * @param testString
     * @return
     */
    ResultDTO getHelloWorld(String testString);

    /**
     * 通过服务端向数据库插入一个简单的数据
     *
     * @param userRequestDTO
     * @return
     */
    ResultDTO addUser(UserRequestDTO userRequestDTO);

    /**
     * 往redis里插入一个简单的数据
     *
     * @param userRequestDTO
     */
    ResultDTO addRedisInfo(@Valid UserRequestDTO userRequestDTO);

    /**
     * 测试本地锁
     */
    ResultDTO testLock();

    ResultDTO redisson();
}

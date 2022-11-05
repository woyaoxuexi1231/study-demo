package com.hundsun.demo.dubbo.consumer.controller;

import com.hundsun.demo.dubbo.common.api.config.ResultAspectConfiguration;
import com.hundsun.demo.dubbo.common.api.model.dto.ResultDTO;
import com.hundsun.demo.dubbo.common.api.utils.ResultDTOBuild;
import com.hundsun.demo.dubbo.consumer.api.service.SimpleService;
import com.hundsun.demo.dubbo.consumer.service.RabbitMqService;
import com.hundsun.demo.dubbo.provider.api.model.request.UserRequestDTO;
import com.hundsun.demo.dubbo.provider.api.model.request.UserSelectReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.dubbodemo.consumer.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:21
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@RestController
@RequestMapping("/test")
@Validated
public class TestController {

    @Autowired
    SimpleService simpleService;

    @Autowired
    ResultAspectConfiguration resultAspectConfiguration;

    @Autowired
    RabbitMqService rabbitMqService;

    @RequestMapping("/hello")
    public ResultDTO<?> getHelloWorld(String hello) {
        return simpleService.getHelloWorld(hello);
    }

    @RequestMapping("/addUser")
    public ResultDTO<?> addUser(@RequestBody UserRequestDTO userRequestDTO) {
        return simpleService.addUser(userRequestDTO);
    }

    @RequestMapping("/addRedisInfo")
    public ResultDTO<?> addRedisInfo(@RequestBody UserRequestDTO userRequestDTO) {
        return simpleService.addRedisInfo(userRequestDTO);
    }

    @RequestMapping("/testLock")
    public ResultDTO<?> testLock() {
        return simpleService.testLock();
    }

    @RequestMapping("/redisson")
    public ResultDTO<?> redisson() {
        return simpleService.redisson();
    }

    @RequestMapping("/testResultConfig")
    public ResultDTO<?> testResultAspectConfiguration() {
        System.out.println(resultAspectConfiguration.getScanRange());
        return ResultDTOBuild.resultDefaultBuild();
    }

    @RequestMapping("/sentSampleMsg")
    public ResultDTO<?> sentSampleMsg() {
        return rabbitMqService.sentSampleMsg();
    }

    /**
     * 查询用户信息
     *
     * @param req req
     * @return result
     */
    @RequestMapping("/getUser")
    public ResultDTO<?> getUser(@RequestBody UserSelectReqDTO req) {
        return simpleService.selectUser(req);
    }

}

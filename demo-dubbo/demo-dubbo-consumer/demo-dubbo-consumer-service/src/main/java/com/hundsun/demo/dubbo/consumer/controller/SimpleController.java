package com.hundsun.demo.dubbo.consumer.controller;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.dubbo.consumer.api.service.SimpleService;
import com.hundsun.demo.dubbo.consumer.service.RabbitMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.dubbodemo.consumer.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:21
 */
@RestController
@RequestMapping("/dubbo")
@Validated
public class SimpleController {

    @Autowired
    SimpleService simpleService;

    @Autowired
    RabbitMqService rabbitMqService;

    @GetMapping("/simpleRpcInvoke")
    public ResultDTO<?> simpleRpcInvoke() {
        return simpleService.simpleRpcInvoke();
    }

    @RequestMapping("/sentSampleMsg")
    public ResultDTO<?> sentSampleMsg() {
        return rabbitMqService.sentSampleMsg();
    }

}

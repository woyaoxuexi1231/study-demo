package com.hundsun.demo.dubbo.provider.controller;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.dubbo.provider.api.service.RabbitMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.dubbo.provider.controller
 * @className: SimpleController
 * @description:
 * @author: h1123
 * @createDate: 2023/3/11 0:22
 */

@RestController
@RequestMapping("/provider")
@Validated
public class SimpleController {

    @Autowired
    RabbitMqService rabbitMqService;

    @RequestMapping("/sentSampleMsg")
    public ResultDTO<?> sentSampleMsg(Integer msgNum) {
        if (Objects.isNull(msgNum) || msgNum < 0) {
            msgNum = 1;
        }
        for (int i = 0; i < msgNum; i++) {
            rabbitMqService.sentSampleMsg();
        }
        return new ResultDTO<>();
    }

}

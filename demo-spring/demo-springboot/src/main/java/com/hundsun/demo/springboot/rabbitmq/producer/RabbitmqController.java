package com.hundsun.demo.springboot.rabbitmq.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.rabbitmq.producer
 * @className: RabbitmqController
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/15 23:57
 */

@RestController
@RequestMapping(value = "/rabbimq")
public class RabbitmqController {

    @Autowired
    RabbitmqService rabbitmqService;

    @GetMapping(value = "/sendMsg")
    public void sendMsg() {
        rabbitmqService.sentSampleMsg();
    }
}

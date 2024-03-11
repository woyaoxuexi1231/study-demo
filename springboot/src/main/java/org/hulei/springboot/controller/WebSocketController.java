package org.hulei.springboot.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: org.hulei.springboot.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-03-11 16:04
 */

@Controller
public class WebSocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return String.format("Hello, %s !", message);
    }

}

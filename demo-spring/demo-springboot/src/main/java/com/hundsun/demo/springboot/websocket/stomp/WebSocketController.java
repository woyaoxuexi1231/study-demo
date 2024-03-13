package com.hundsun.demo.springboot.websocket.stomp;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

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
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

}

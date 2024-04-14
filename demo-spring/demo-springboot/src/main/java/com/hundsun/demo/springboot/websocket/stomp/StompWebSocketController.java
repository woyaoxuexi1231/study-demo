package com.hundsun.demo.springboot.websocket.stomp;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Controller
public class StompWebSocketController {

    // 这个注解指定了处理消息的目的地。当客户端发送消息到 “/app/hello” 这个目的地时，Spring 将调用这个方法来处理消息。
    @MessageMapping("/app/hello")
    //  这个注解指定了处理完消息后将结果发送到的目的地。在这个例子中，处理完消息后，结果会被发送到 “/topic/greetings” 这个主题下，这样所有订阅了这个主题的客户端都会收到这个消息。
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        log.info("收到消息:{}", message);
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

}

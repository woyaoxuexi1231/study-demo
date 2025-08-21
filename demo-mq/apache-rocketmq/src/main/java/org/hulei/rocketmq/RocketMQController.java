package org.hulei.rocketmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rocketmq")
public class RocketMQController {

    @Autowired
    private RocketMQProducerService producerService;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String topic, @RequestParam String message) {
        producerService.sendMessage(topic, message);
        return "✅ 消息已发送到 Topic: " + topic + ", 内容: " + message;
    }

    @PostMapping("/sendWithTag")
    public String sendMessageWithTag(
            @RequestParam String topic,
            @RequestParam String tag,
            @RequestParam String message) {
        producerService.sendMessageWithTag(topic, tag, message);
        return "✅ 消息已发送到 Topic: " + topic + ", Tag: " + tag + ", 内容: " + message;
    }
}
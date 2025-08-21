package org.hulei.springboot.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message) {
        kafkaProducerService.sendMessage(message);
        return "Message sent: " + message;
    }

    @PostMapping("/sendWithKey")
    public String sendMessageWithKey(@RequestParam("key") String key,
                                     @RequestParam("message") String message) {
        kafkaProducerService.sendMessageWithKey(key, message);
        return "Message with key sent: key=" + key + ", value=" + message;
    }
}
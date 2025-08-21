package org.hulei.rocketmq;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RocketMQProducerService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // 发送普通消息到指定 topic
    public void sendMessage(String topic, String message) {
        String destination = topic; // 可以是 "topicName" 或 "topicName:tags"
        rocketMQTemplate.convertAndSend(destination, message);
        System.out.println("🟢 [Producer] 发送消息到 RocketMQ Topic: " + destination + ", 内容: " + message);
    }

    // 如果需要带 tag，比如 "topicName:tagA"
    public void sendMessageWithTag(String topic, String tag, String message) {
        String destination = topic + ":" + tag;
        rocketMQTemplate.convertAndSend(destination, message);
        System.out.println("🟢 [Producer] 发送消息到 Topic: " + destination + ", 内容: " + message);
    }
}
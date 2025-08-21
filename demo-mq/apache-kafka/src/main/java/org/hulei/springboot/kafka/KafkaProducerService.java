package org.hulei.springboot.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    // 注入KafkaTemplate，Spring Boot会自动配置它
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // 从配置文件中注入topic名称
    @Value("${myapp.kafka.topic}")
    private String topicName;

    /**
     * 发送消息（异步方式）
     * @param message 要发送的消息内容
     */
    public void sendMessage(String message) {
        // 使用CompletableFuture可以处理发送成功或失败的回调
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
        future.completable().whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.err.println("Unable to send message=[" +
                        message + "] due to : " + ex.getMessage());
            }
        });
    }

    /**
     * 发送带key的消息，key用于决定消息被发送到哪个分区
     * @param key 消息的键
     * @param message 消息的值
     */
    public void sendMessageWithKey(String key, String message) {
        kafkaTemplate.send(topicName, key, message)
                .completable()
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        System.out.println("Sent message with key=[" + key + "], value=[" + message +
                                "] to partition=[" + result.getRecordMetadata().partition() + "]");
                    } else {
                        System.err.println("Failed to send message: " + ex.getMessage());
                    }
                });
    }
}
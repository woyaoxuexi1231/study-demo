package org.hulei.springboot.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    /**
     * 最简单的监听器，监听指定的topic
     * @param message 接收到的消息内容
     */
    @KafkaListener(topics = "${myapp.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        System.out.println("Received Message in group 'my-springboot-group': " + message);
    }

    /**
     * 更高级的监听器，可以获取消息的元数据（如offset, partition, key等）
     * @param message 消息体（Payload）
     * @param key 消息的键
     * @param partition 消息所在的分区
     * @param offset 消息的偏移量
     * @param topic 主题
     */
    @KafkaListener(topics = "${myapp.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenWithMetadata(@Payload String message,
                                   @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                   @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                   @Header(KafkaHeaders.OFFSET) long offset,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println(String.format("Received message [%s] with key [%s] from topic [%s], partition [%d], offset [%d]",
                message, key, topic, partition, offset));
    }

    // 注意：如果配置中设置了 `ack-mode: manual_immediate`，则需要手动提交偏移量
    // 下面是一个手动提交偏移量的例子（需要将配置中的ack-mode改为manual_immediate，并启用此方法）
    /*
    @KafkaListener(topics = "${myapp.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenForManualAck(String message, Acknowledgment ack) {
        System.out.println("Received Message: " + message);
        // 业务处理逻辑...

        // 手动确认消息，提交偏移量
        ack.acknowledge();
        System.out.println("Message acknowledged");
    }
    */
}
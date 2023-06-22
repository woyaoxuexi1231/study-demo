package com.hundsun.demo.java.mq.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.mq.kafka
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-06-22 14:50
 */

public class ProducerFastStart {

    public static final String brokerList = "192.168.80.128:9092";
    public static final String topic = "topic-demo";

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("bootstrap.servers", brokerList);


        KafkaProducer<String, String> producer =
                new KafkaProducer<>(properties);
        ProducerRecord<String, String> record =
                new ProducerRecord<>(topic, "hello, Kafka!");
        try {
            producer.send(record);
//            producer.send(record).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.close();
    }
}

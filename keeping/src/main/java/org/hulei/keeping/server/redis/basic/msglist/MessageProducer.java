package org.hulei.keeping.server.redis.basic.msglist;

import redis.clients.jedis.Jedis;

public class MessageProducer {
    private final String host;
    private final int port;

    public MessageProducer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void publishMessage(String listName, String message) {
        try (Jedis jedis = new Jedis(host, port)) {
            jedis.auth("123456");
            jedis.lpush(listName, message);
            System.out.println("消息发布: " + message);
        }
    }

    public static void main(String[] args) {
        MessageProducer producer = new MessageProducer("192.168.80.128", 6379);
        producer.publishMessage("messageQueue", "Hello, Redis!");
        // 添加更多的消息，如果需要
    }
}
package org.hulei.redis.basic.msglist;

import redis.clients.jedis.Jedis;

import java.util.List;

public class MessageConsumer extends Thread {
    private final Jedis jedis;
    private final String listName;

    public MessageConsumer(String host, int port, String listName) {
        this.jedis = new Jedis(host, port);
        jedis.auth("123456");
        this.listName = listName;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("等待消息...");
            // 第二个参数是超时时间，0代表永久阻塞直到有消息
            // brpop 返回一个键值对列表，第0个元素是列表名称，第1个元素是弹出的值。
            List<String> message = jedis.brpop(0, listName);
            if (message != null) {
                System.out.println("收到消息: " + message.get(1));
            }
        }
    }

    public static void main(String[] args) {
        MessageConsumer consumer = new MessageConsumer("192.168.80.128", 6379, "messageQueue");
        consumer.start();
        // 可以启动多个消费者实例，如果需要
    }
}
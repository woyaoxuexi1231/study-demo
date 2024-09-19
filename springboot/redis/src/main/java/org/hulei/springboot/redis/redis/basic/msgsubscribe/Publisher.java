package org.hulei.keeping.server.redis.basic.msgsubscribe;

import redis.clients.jedis.Jedis;

import java.util.Scanner;

public class Publisher {

    public static void main(String[] args) {

        String channel = "testChannel";

        try (Jedis jedis = new Jedis("192.168.80.128", 6379)) {
            jedis.auth("123456");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String msg = scanner.nextLine();
                if ("exit".equalsIgnoreCase(msg)) {
                    break;
                }
                Long count = jedis.publish(channel, msg);
                System.out.printf("此消息有%d个消费者收到%n", count);
            }
        }

        System.out.println("结束");
    }
}
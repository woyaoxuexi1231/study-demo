package org.hulei.redis.basic.msgsubscribe;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.locks.LockSupport;

@SuppressWarnings("CallToPrintStackTrace")
public class Subscriber extends Thread {

    private final Jedis jedis;
    private final JedisPubSub subscriber;
    private final String channelName;

    public Subscriber(String host, int port, String auth, String channelName) {
        this.jedis = new Jedis(host, port);
        jedis.auth(auth);
        this.subscriber = new JedisPubSub() {
            /**
             * 此信道上如果有新消息,那么会触发此方法
             * 1. 新的订阅者不会收到订阅前的消息,redis不会把消息持久化
             * 2. 在处理完当前消息的情况下不会再触发下一次消息的接收,这种情况下redis会把消息暂存,直到把消息发完
             *
             * @param channel 信道,类似topic
             * @param message 消息
             */
            @Override
            public void onMessage(String channel, String message) {
                System.out.println("接收到消息: Channel: " + channel + ", Msg: " + message);
                LockSupport.parkNanos(10L * 1000 * 1000 * 1000);
            }
        };
        this.channelName = channelName;
    }

    @Override
    public void run() {
        try {
            System.out.println("订阅频道: " + channelName);
            jedis.subscribe(subscriber, channelName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    public static void main(String[] args) {
        String channel = "testChannel";
        // 启动订阅者监听
        Subscriber subscriberThread = new Subscriber("192.168.80.128", 6379, "123456", channel);
        subscriberThread.start();
    }
}
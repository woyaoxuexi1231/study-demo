package org.hulei.keeping.server.redis.basic.msgsubscribe;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class SubscriberListeningThread extends Thread {

    private final Jedis jedis;
    private final JedisPubSub subscriber;
    private final String channelName;

    public SubscriberListeningThread(String host, int port, String auth, String channelName) {

        this.jedis = new Jedis(host, port);
        jedis.auth(auth);

        this.subscriber = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                System.out.println("接收到消息: Channel: " + channel + ", Msg: " + message);
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
}
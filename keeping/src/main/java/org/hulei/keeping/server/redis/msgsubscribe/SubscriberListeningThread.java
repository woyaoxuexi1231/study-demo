package org.hulei.keeping.server.redis.msgsubscribe;

import redis.clients.jedis.Jedis;

public class SubscriberListeningThread extends Thread {

    private final Jedis jedis;
    private final RedisSubscriber subscriber;
    private final String channelName;

    public SubscriberListeningThread(String host, int port, String auth, String channelName) {
        this.jedis = new Jedis(host, port);
        jedis.auth(auth);
        this.subscriber = new RedisSubscriber();
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
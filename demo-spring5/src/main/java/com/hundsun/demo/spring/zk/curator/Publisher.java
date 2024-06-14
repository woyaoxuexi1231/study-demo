package com.hundsun.demo.spring.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Publisher {
    private CuratorFramework client;
    private String path;

    public Publisher(String connectionString, String path) {
        this.client = CuratorFrameworkFactory.newClient(connectionString, new ExponentialBackoffRetry(1000, 3));
        this.path = path;
        this.client.start();
    }

    public void publish(String message) throws Exception {
        // 确保节点存在
        if (client.checkExists().forPath(path) == null) {
            client.create().creatingParentsIfNeeded().forPath(path, message.getBytes());
        } else {
            client.setData().forPath(path, message.getBytes());
        }
    }

    public static void main(String[] args) throws Exception {
        Publisher publisher = new Publisher("192.168.80.128:2181", "/pubsub/example");
        while (true) {
            String message = "Message " + System.currentTimeMillis();
            publisher.publish(message);
            System.out.println("Published: " + message);
            Thread.sleep(1000); // 发布消息的间隔
        }
    }
}
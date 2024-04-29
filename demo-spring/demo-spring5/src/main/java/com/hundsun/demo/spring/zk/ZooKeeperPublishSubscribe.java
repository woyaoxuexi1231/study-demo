package com.hundsun.demo.spring.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZooKeeperPublishSubscribe {

    private static ZooKeeper zooKeeper;
    private static final String HOST = "192.168.80.128:2181";
    private static final String PUBLISH_NODE = "/publisher";
    private static final CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        zooKeeper = connect();
        if (zooKeeper.exists(PUBLISH_NODE, false) == null) {
            zooKeeper.create(PUBLISH_NODE, "Initial Data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        subscribe(PUBLISH_NODE);

        // Simulate publishing data
        publish(PUBLISH_NODE, "Hello, Subscribers!");

        Thread.sleep(3000); // Just to wait for watcher to get notified

        zooKeeper.close();
    }

    private static ZooKeeper connect() throws IOException {
        return new ZooKeeper(HOST, 2000, event -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                latch.countDown();
            }
        });
    }

    private static void publish(String nodePath, String data) throws KeeperException, InterruptedException {
        zooKeeper.setData(nodePath, data.getBytes(), -1);
    }

    private static void subscribe(String nodePath) throws KeeperException, InterruptedException {
        latch.await();
        Stat exists = zooKeeper.exists(nodePath, event -> {
            if (event.getType() == EventType.NodeDataChanged) {
                try {
                    byte[] newData = zooKeeper.getData(nodePath, false, null);
                    System.out.println("Notification Received! New Data: " + new String(newData));
                    subscribe(nodePath);  // Re-subscribe to get further updates
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        if (exists != null) {
            byte[] data = zooKeeper.getData(nodePath, false, null);
            System.out.println("Current Data: " + new String(data));
        }
    }
}
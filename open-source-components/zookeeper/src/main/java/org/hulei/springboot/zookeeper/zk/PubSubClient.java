package org.hulei.springboot.zookeeper.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

public class PubSubClient {

    private static ZooKeeper zooKeeper;
    private static final String HOST = "192.168.80.128:2181";
    private static final String PUBLISH_NODE = "/publisher";
    private static final CountDownLatch CONNECTED_LATCH = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        try {

            // 创建连接
            zooKeeper = connect();

            // 创建一个发布目录,相当于队列
            if (zooKeeper.exists(PUBLISH_NODE, false) == null) {
                // Ids.OPEN_ACL_UNSAFE指定了节点的ACL（Access Control List），此处为开放权限（允许任何人读写）
                // CreateMode.PERSISTENT指定了节点类型为持久节点，即一旦创建将一直存在，直到被删除。
                zooKeeper.create(PUBLISH_NODE, "Initial Data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            subscribe(PUBLISH_NODE);

            Thread publisherThread = new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    while (true) {
                        String input = reader.readLine();
                        publish(PUBLISH_NODE, input);
                        if (input.equalsIgnoreCase("exit")) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            publisherThread.start();
            publisherThread.join();

        } finally {
            if (zooKeeper != null) {
                zooKeeper.close();
            }
        }
    }

    private static ZooKeeper connect() throws IOException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(HOST, 2000, event -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                CONNECTED_LATCH.countDown();
            }
        });
        CONNECTED_LATCH.await();
        return zk;
    }

    private static void publish(String nodePath, String data) throws KeeperException, InterruptedException {
        zooKeeper.setData(nodePath, data.getBytes(), -1);
    }

    private static void subscribe(String nodePath) throws KeeperException, InterruptedException {
        // 节点存在会返回一个 Stat, 节点如果不存在那么此方法会返回 null
        Stat exists = zooKeeper.exists(
                nodePath,
                // 定义一个 Watcher, 在节点状态发生变化的时候接收通知
                // 节点变化包括 节点的创建,节点的删除,节点数据的更新,子节点的变化
                event -> {
                    // 这里我们监控节点的数据变化
                    if (event.getType() == EventType.NodeDataChanged) {
                        try {
                            byte[] newData = zooKeeper.getData(nodePath, false, null);
                            System.out.println("Receive Message: " + new String(newData));
                            // 这里递归调用并不会引起栈溢出, 这里仅仅是把这个注册事件放入事件队列中
                            subscribe(nodePath);  // Re-subscribe to get further updates
                        } catch (KeeperException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

        if (exists != null) {
            byte[] data = zooKeeper.getData(nodePath, false, null);
            System.out.println("Subscribed Data: " + new String(data));
        }
    }
}
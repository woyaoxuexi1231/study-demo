package org.hulei.springboot.zookeeper.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZooKeeperClinet {

    private static final String CONNECTION_STRING = "192.168.80.128:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        // 创建一个新的ZooKeeper实例
        zooKeeper = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 处理连接状态变化
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("Connected to ZooKeeper");
                }
            }
        });

        // 创建一个znode
        String path = "/example";
        byte[] data = "Hello ZooKeeper".getBytes();
        zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // 读取znode数据
        byte[] retrievedData = zooKeeper.getData(path, false, null);
        System.out.println("Retrieved data: " + new String(retrievedData));

        // 删除znode
        zooKeeper.delete(path, -1);

        // 关闭ZooKeeper连接
        zooKeeper.close();
    }
}

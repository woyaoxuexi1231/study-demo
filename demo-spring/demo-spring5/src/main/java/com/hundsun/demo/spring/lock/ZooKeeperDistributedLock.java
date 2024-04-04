package com.hundsun.demo.spring.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ZooKeeperDistributedLock {

    private static final String ZK_CONNECTION_STRING = "192.168.80.128:2181";
    private static final int SESSION_TIMEOUT_MS = 5000;
    private static final String LOCK_PATH = "/lock";

    public static void main(String[] args) throws Exception {

        // 创建 ZooKeeper 客户端
        ZooKeeper zooKeeper = new ZooKeeper(ZK_CONNECTION_STRING, SESSION_TIMEOUT_MS, (event) -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                System.out.println("Connected to ZooKeeper");
            }
        });

        // 创建锁节点
        try {
            String lockPath = zooKeeper.create(LOCK_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException.NodeExistsException e) {
            System.out.println("主节点已存在,跳过创建");
        }

        // 创建子节点
        // ZooDefs.Ids.OPEN_ACL_UNSAFE: 是节点的 ACL（访问控制列表），使用 OPEN_ACL_UNSAFE 表示对所有用户开放权限，允许任何用户对节点进行读写操作。
        // CreateMode.EPHEMERAL_SEQUENTIAL: 是节点的创建模式，使用 EPHEMERAL_SEQUENTIAL 表示创建临时顺序节点。临时节点在客户端断开连接时会自动删除，而顺序节点会在节点路径后自动添加一个数字后缀，确保节点名称的唯一性。
        String lockNodePath = zooKeeper.create(LOCK_PATH + "/lock_", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);


        while (true) {

            // 获取所有临时顺序节点，并按照节点名称排序
            List<String> children = zooKeeper.getChildren(LOCK_PATH, false);
            Collections.sort(children);
            // 等待获取锁
            CountDownLatch latch = new CountDownLatch(1);

            // 判断当前节点是否为最小序号节点，如果是，则获取锁成功
            String smallestNode = children.get(0);
            if (lockNodePath.equals(LOCK_PATH + "/" + smallestNode)) {
                System.out.println("获取锁成功，执行业务逻辑...");
                Thread.sleep(30000);
                // 在这里执行业务逻辑
                // 业务逻辑执行完毕后，删除锁节点
                zooKeeper.delete(lockNodePath, -1);
                break;
            } else {
                // 如果当前节点不是最小序号节点，则监听前一个节点
                int index = Collections.binarySearch(children, lockNodePath.substring(lockNodePath.lastIndexOf("/") + 1));
                String previousNode = children.get(index - 1);
                System.out.println("等待获取锁...");
                zooKeeper.exists(LOCK_PATH + "/" + previousNode, event -> {
                    // 前一个节点被删除，尝试获取锁
                    if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                        try {
                            System.out.println("前一个节点已删除，尝试获取锁...");
                            latch.countDown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            latch.await();
        }

        // 关闭 ZooKeeper 客户端
        zooKeeper.close();
    }
}

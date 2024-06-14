package com.hundsun.demo.spring.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorLock {

    private static final String ZK_CONNECTION_STRING = "192.168.80.128:2181";
    private static final int SESSION_TIMEOUT_MS = 5000;
    private static final String LOCK_PATH = "/my_lock";

    public static void main(String[] args) throws Exception {
        // 创建 CuratorFramework 客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_CONNECTION_STRING,
                new ExponentialBackoffRetry(1000, 3));
        /*
        这里启动出现了一个报错
        The version of ZooKeeper being used doesn't support Container nodes. CreateMode.PERSISTENT will be used instead.
        这个警告是由于 Curator 检测到你正在使用的 ZooKeeper 版本不支持容器节点（Container nodes）。容器节点是 ZooKeeper 3.5.x 引入的新特性，允许节点可以包含子节点，并且支持原子性的节点创建和删除操作。然而，你使用的 ZooKeeper 版本可能不支持这个功能，所以 Curator 将使用旧的方式来创建持久节点（CreateMode.PERSISTENT）。
        这个警告本身并不会导致程序出现错误，只是告诉你正在使用的 ZooKeeper 版本不支持某些特性。如果你想要使用容器节点的功能，你可以考虑升级你的 ZooKeeper 版本到支持容器节点的版本。
        另外，如果你不需要使用容器节点的功能，你可以忽略这个警告，程序仍然可以正常运行。
         */
        client.start();

        // 创建分布式锁
        InterProcessMutex lock = new InterProcessMutex(client, LOCK_PATH);
        boolean isLocked = false;
        try {
            // 尝试获取锁
            if (lock.acquire(5000, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                isLocked = true;
                System.out.println("获取锁成功，执行业务逻辑...");
                Thread.sleep(10000);
                // 在这里执行业务逻辑
            } else {
                System.out.println("获取锁失败，未能执行业务逻辑");
            }
        } finally {
            // 释放锁
            if (isLocked) {
                lock.release();
                System.out.println("解锁成功");
            }
            client.close();
        }
    }
}

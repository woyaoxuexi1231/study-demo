package com.hundsun.demo.spring.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorExample {

    private static final String CONNECTION_STRING = "192.168.80.128:2181";
    private static final int CONNECTION_TIMEOUT = 15000; // 连接超时时间为15秒
    private static final int SESSION_TIMEOUT = 20000;   // 会话超时时间为20秒

    public static void main(String[] args) throws Exception {
        // 创建 CuratorFramework 客户端
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString(CONNECTION_STRING)
                .connectionTimeoutMs(CONNECTION_TIMEOUT)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        // 启动客户端连接
        curator.start();

        // 创建一个znode
        String path = "/example";
        byte[] data = "Hello Curator".getBytes();
        curator.create().creatingParentsIfNeeded().forPath(path, data);

        // 读取znode数据
        byte[] retrievedData = curator.getData().forPath(path);
        System.out.println("Retrieved data: " + new String(retrievedData));

        // 删除znode
        curator.delete().deletingChildrenIfNeeded().forPath(path);

        // 关闭CuratorFramework客户端
        curator.close();
    }
}

package org.hulei.keeping.server.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class Subscriber {
    private CuratorFramework client;

    public Subscriber(String connectionString, String path) throws Exception {
        this.client = CuratorFrameworkFactory.newClient(connectionString, new ExponentialBackoffRetry(1000, 3));
        this.client.start();

        ensurePathExists(path);  // 确保节点存在

        client.getData().watched().forPath(path);  // 设置初始Watcher
    }

    private void ensurePathExists(String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        if (stat == null) {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, new byte[0]);
        }
        // 添加节点监听器
        addListener(path);
    }

    private void addListener(String path) throws Exception {
        client.getData().usingWatcher((CuratorWatcher) event -> {
            System.out.println("Received message: " + new String(client.getData().forPath(path)));
            // 重新设置Watcher
            addListener(path);
        }).forPath(path);
    }

    public static void main(String[] args) throws Exception {
        new Subscriber("192.168.80.128:2181", "/pubsub/example");
        Thread.sleep(Long.MAX_VALUE); // 保持订阅者运行
    }
}
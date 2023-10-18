package com.hundsun.demo.java;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java
 * @className: ZookeeperTest
 * @description:
 * @author: hl
 * @createDate: 2023/9/19 23:08
 */

public class ZookeeperNote {
    /*
     * 错误`java.io.IOException: No snapshot found, but there are log entries. Something is broken!`通常是由Zookeeper的数据文件损坏引起的。Zookeeper维护了事务日志和快照文件来保存数据。在启动时，Zookeeper会读取这些文件并恢复数据。然而，如果其中一个文件被损坏或丢失，就可能会出现这个错误。
     *
     * 以下是一些可能的解决方案：
     * 1. 如果你的数据文件不重要，你可以直接删除Zookeeper配置文件中配置的`dataDir`和`dataLogDir`指定的目录，然后重新启动Zookeeper。
     * 2. 找到你的`zoo.conf`中配置的`dataDir`和`dataLogDir`路径。然后删除这两个目录下的`version-2`文件夹。
     * 3. 检查你的Kafka文件夹所在的驱动器上是否有`tmp/zookeeper`文件夹，并删除`tmp`文件夹，一旦再次运行zookeeper，它将自动为你创建。
     * 4. 清理了`/tmp/zookeeper-data`并再次运行后，如果你得到了错误`No snapshot found, but there are log entries`，你只需要清理一下`/tmp/zookeeper-txn-logs`上的数据。
     *
     * 在尝试这些解决方案之前，请记得备份任何重要的数据。
     */
}

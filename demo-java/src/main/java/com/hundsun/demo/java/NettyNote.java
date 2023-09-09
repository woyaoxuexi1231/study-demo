package com.hundsun.demo.java;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-09-06 16:51
 */

public class NettyNote {
    /*
     * 高并发 IO 的底层原理
     * 1. IO 读写的基本原理
     *  会用到底层提供的 read 和 write 两大系统调用, read 和 write 不会直接在物理内存上写入/读取数据, 都是通过缓冲区进行的
     *  数据在内核缓冲区和用户缓冲区之间流动,  网卡 -> 内核缓冲区 -> 用户缓冲区
     * 2. 四种主要的 IO 模型
     *  同步IO, 同步非阻塞IO, IO多路复用, 异步IO
     *  同步IO: 从发起 read请求开始, 一直到内核缓冲区的数据完整的复制到用户缓冲区才接触阻塞
     *  同步非阻塞IO: 在内核数据到达之前, 用户线程一直轮询进行IO系统调用, 不阻塞. 一旦内核数据到达, 一直到用户进程获取到数据才会解除阻塞状态
     *  IO多路复用: 提供选择器注册, 可以同时监控多个socket状态, 轮询访问列表以得到就绪的socket, 然后进行数据复制(从内核到用户缓冲的复制过程仍需要阻塞用户线程)
     *  异步IO: 整个过程都是异步的, 这种IO需要操作系统的底层支持
     * 3. 通过合理的配置来支持百万级别的并发连接
     *
     *
     * Java NIO核心
     * 1. Java NIO 简介
     * 2. NIO buffer
     *  一个非线程安全的内存块, 可以写入数据, 也可以读取数据.
     * 3. NIO channel
     * 4. NIO select
     *
     * Reactor模式
     * 1. 单线程 Reactor模式
     * 2. 多线程 Reactor模式
     * 3. Reactor模式的优缺点
     *
     * Netty核心原理
     * 1. Netty里的 Reactor模式
     * 2. bootstrap
     * 3. channel
     * 4. handler
     * 5. pipeline
     * 6. bytebuf
     * 7. netty的零拷贝
     *
     * */
}

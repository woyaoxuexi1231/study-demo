package com.crazymakercircle.netty.basic;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/

import com.crazymakercircle.netty.NettyDemoConfig;
import com.crazymakercircle.util.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyDiscardServer {

    private final int serverPort;
    /*
    这是一个服务引导类, 是一个组装和集成器, 职责是将不同的 Netty 组件组装在一起.
    ServerBootstrap 能够按照不同的应用场景的需要为组件设置好基础性的参数, 最后帮助快速实现 Netty 服务器的监听和启动.
     */
    ServerBootstrap b = new ServerBootstrap();

    public NettyDiscardServer(int port) {
        this.serverPort = port;
    }

    public void runServer() {
        // 这个负责处理连接监听 IO 事件
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        // 这个负责处理数据传输
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            // 1. 设置 reactor 线程组
            b.group(bossLoopGroup, workerLoopGroup);
            // 2. 设置 nio 类型的 channel, NioServerSocketChannel-异步非阻塞 TCP socket 服务监听通道
            b.channel(NioServerSocketChannel.class);
            // 3. 设置监听端口
            b.localAddress(serverPort);
            // 4. 设置通道的参数
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            // 5. 装配子通道流水线
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                // 有连接到达时会创建一个 channel
                protected void initChannel(SocketChannel ch) throws Exception {
                    // pipeline 管理子通道 channel 中的 Handler
                    // 向子 channel 流水线添加一个 handler 处理器
                    ch.pipeline().addLast(new NettyDiscardHandler());
                }
            });
            // 6. 开始绑定 server
            // 通过调用 sync 同步方法阻塞直到绑定成功
            ChannelFuture channelFuture = b.bind().sync();
            Logger.info("服务器启动成功, 监听端口: " +
                    channelFuture.channel().localAddress());

            // 7. 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            workerLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        int port = NettyDemoConfig.SOCKET_SERVER_PORT;
        new NettyDiscardServer(port).runServer();
    }
}
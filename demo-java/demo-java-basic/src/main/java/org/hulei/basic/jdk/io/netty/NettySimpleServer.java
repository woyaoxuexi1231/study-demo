package org.hulei.basic.jdk.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.hulei.basic.jdk.io.NIOUtil;

/**
 * @author hulei
 * @since 2024/9/14 12:10
 */

public class NettySimpleServer {

    public static void main(String[] args) {

        /*
        netty提供的一个便利的工厂类
        可以通过它来完成 netty 的客户端或服务端的 netty 的组装，以及 netty 程序的初始化和启动执行
        netty官方的解释：可以完全不用这个 bootstrap 类，一点一点去创建通道、完成各种设置和启动注册到 eventLoop，然后开始事件轮询和处理，但很麻烦没必要。
        ServerBootstrap 服务器专用
        Bootstrap 客户端专用

        文件描述符：文件描述符是一个非负整数，用于标识进程打开的文件或I/O资源，是操作系统访问文件的抽象句柄。
         */
        ServerBootstrap bootstrap = new ServerBootstrap();

        /*
        NioEventLoopGroup：netty中对应多线程 nio 通信的应用场景的 反应器(Reactor)
        负责查询(select)事件和分发(dispatch)事件

        netty中一个EventLoop相当于一个子反应器subReactor(在reactor中子反应器相当于专门查询IO传输事件的反应器)
        netty通过EventLoopGroup包装多个EventLoop来实现多线程版本的reactor

        构造时通过 nThreads 来确定创建多少个反应器，甚至可以自己传入Executor，这样使多个反应器使用共有的Executor，使用不当会导致线程不足
         */
        // 专门负责新连接IO事件的反应器
        NioEventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        // 专门负责传输通道的IO事件的处理和数据传输，这里没传线程数，默认为最大CPU处理器数量*2
        NioEventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            // 设置父子反应器
            bootstrap.group(bossLoopGroup, workerLoopGroup);
            // 指定 Channel 类型，这里使用 NIO 非阻塞模式。
            bootstrap.channel(NioServerSocketChannel.class);
            // 绑定服务端监听的 本地端口（这里是 8105）。
            bootstrap.localAddress(8105);


            // 设置传输通道的配置选项
            // 设置 TCP 层的心跳机制（SO_KEEPALIVE）。如果客户端长时间无数据交互，TCP 会自动发送心跳包检测连接是否存活。
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            // 配置 服务端 Channel 的 ByteBuf 分配器（用于接收连接的 ServerSocketChannel）。
            // 如果不设置，Netty 4.1+ 默认也是 PooledByteBufAllocator。
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);


            // 配置 子 Channel 的 ByteBuf 分配器（即客户端连接的 SocketChannel）。
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            // 装配子通道流水线，这里可以发现，其实并没有装配父流水线，通过bootstrap其实也可以知道，bootstrap帮我们组装了，因为父流水线只负责接受新连接后创建子通道
            // ChannelInitializer 通道初始化器，在通道初始化的时候会调用提前注册的初始化处理器的 initChannel 方法
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                // 有连接到达时会创建一个channel
                protected void initChannel(SocketChannel ch) throws Exception {
                    // pipeline管理子通道channel中的Handler
                    // 向子channel流水线添加一个handler处理器
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf in = (ByteBuf) msg;
                            try {
                                StringBuilder sb = new StringBuilder();
                                while (in.isReadable()) {
                                    sb.append((char) in.readByte());
                                }
                                NIOUtil.info("收到消息，先不丢弃：" + sb);
                                super.channelRead(ctx, sb);
                            } finally {
                                // Netty 中用于 手动释放引用计数对象（Reference-Counted Object） 的方法，主要作用是 减少对象的引用计数，当计数归零时释放底层资源（如内存）。
                                ReferenceCountUtil.release(msg);
                            }
                        }

                        @Override
                        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                            NIOUtil.info("信道注册了");
                            super.channelRegistered(ctx);
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            NIOUtil.info("信道不再活跃");
                            super.channelInactive(ctx);
                        }
                    });

                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            try {
                                NIOUtil.info("收到消息,丢弃如下:" + msg);
                            } finally {
                                // Netty 中用于 手动释放引用计数对象（Reference-Counted Object） 的方法，主要作用是 减少对象的引用计数，当计数归零时释放底层资源（如内存）。
                                ReferenceCountUtil.release(msg);
                            }
                        }

                        @Override
                        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                            NIOUtil.info("信道注册了");
                            super.channelRegistered(ctx);
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            NIOUtil.info("信道不再活跃");
                            super.channelInactive(ctx);
                        }
                    });
                }
            });

            // 开始绑定server
            // 通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture channelFuture = bootstrap.bind().sync();
            NIOUtil.info(String.format(" 服务器启动成功，监听端口: %s", channelFuture.channel().localAddress()));

            // 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            NIOUtil.info(String.format("netty监听服务出现异常, %s", e.getMessage()));
        } finally {
            bossLoopGroup.shutdownGracefully();
            workerLoopGroup.shutdownGracefully();
        }

    }
}

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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hulei
 * @since 2024/9/14 12:10
 */

@Slf4j
public class NettySimpleServer {

    @SneakyThrows
    public static void main(String[] args) {

        ServerBootstrap bootstrap = new ServerBootstrap();

        NioEventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        bootstrap.group(bossLoopGroup, workerLoopGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.localAddress(8105);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        //5 装配子通道流水线
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            //有连接到达时会创建一个channel
            protected void initChannel(SocketChannel ch) throws Exception {
                // pipeline管理子通道channel中的Handler
                // 向子channel流水线添加一个handler处理器
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                        ByteBuf in = (ByteBuf) msg;
                        try {
                            log.info("收到消息,丢弃如下:");
                            while (in.isReadable()) {
                                System.out.print((char) in.readByte());
                            }
                            System.out.println();
                        } finally {
                            ReferenceCountUtil.release(msg);
                        }
                    }

                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        log.info("信道注册了");
                        super.channelRegistered(ctx);
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        log.info("信道不再活跃");
                        super.channelInactive(ctx);
                    }
                });
            }
        });
        // 6 开始绑定server
        // 通过调用sync同步方法阻塞直到绑定成功
        ChannelFuture channelFuture = bootstrap.bind().sync();
        log.info(" 服务器启动成功，监听端口: {}", channelFuture.channel().localAddress());

        // 7 等待通道关闭的异步任务结束
        // 服务监听通道会一直等待通道关闭的异步任务结束
        ChannelFuture closeFuture = channelFuture.channel().closeFuture();
        closeFuture.sync();
    }
}

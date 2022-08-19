package com.codexiangli.im.server;

import com.codexiangli.im.common.util.netty.EventLoopUtil;
import com.codexiangli.im.server.initializer.ImServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author lixiang
 * @since 2022/8/12
 */
@Slf4j
public class Server {

    private final EventLoopGroup acceptorGroup;
    private final EventLoopGroup workerGroup;
    private final ServerBootstrap defaultServerBootstrap;


    public Server(EventLoopGroup eventLoopGroup) {
        // todo 支持参数配置 Config
        final DefaultThreadFactory acceptorThreadFactory = new DefaultThreadFactory("server-acceptor");
        this.acceptorGroup = EventLoopUtil.newEventLoopGroup(1, false, acceptorThreadFactory);
        this.workerGroup = eventLoopGroup;
        this.defaultServerBootstrap = defaultServerBootstrap();
    }


    public void start() throws IOException {
        ChannelFuture channelFuture;
        try {
            defaultServerBootstrap.childHandler(new ImServerChannelInitializer());
            channelFuture = defaultServerBootstrap.bind();
            channelFuture.addListener((Future<? super Void> future) -> log.info("Successfully start server"));
            channelFuture.sync().channel().closeFuture().sync();
        } catch (Exception e) {
            throw new IOException("Failed to start server ", e);
        } finally {
            workerGroup.shutdownGracefully();
            acceptorGroup.shutdownGracefully();
        }
    }

    private ServerBootstrap defaultServerBootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(acceptorGroup, workerGroup);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.RCVBUF_ALLOCATOR,
                        new AdaptiveRecvByteBufAllocator(1024, 16 * 1024, 1 * 1024 * 1024))
                .localAddress(new InetSocketAddress(8000))
                //保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .channel(EventLoopUtil.getServerSocketChannelClass(workerGroup));
        EventLoopUtil.enableTriggeredMode(bootstrap);
        return bootstrap;
    }
}

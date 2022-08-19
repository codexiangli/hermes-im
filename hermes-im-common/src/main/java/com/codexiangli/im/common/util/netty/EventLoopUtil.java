package com.codexiangli.im.common.util.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SelectStrategy;
import io.netty.channel.epoll.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ThreadFactory;

/**
 * @author lixiang
 * @since 2022/8/12
 */
public class EventLoopUtil {

    /**
     * @return an EventLoopGroup suitable for the current platform
     */
    public static EventLoopGroup newEventLoopGroup(int nThreads, boolean enableBusyWait, ThreadFactory threadFactory) {
        // 支持 epoll 的话就使用 EpollEventLoopGroup，否则使用 NioEventLoopGroup (select)
        if (Epoll.isAvailable()) {
            if (!enableBusyWait) {
                // Regular Epoll based event loop
                return new EpollEventLoopGroup(nThreads, threadFactory);
            }

            // With low latency setting, put the Netty event loop on busy-wait loop to reduce cost of
            // context switches
            EpollEventLoopGroup eventLoopGroup = new EpollEventLoopGroup(nThreads, threadFactory,
                    () -> (selectSupplier, hasTasks) -> SelectStrategy.BUSY_WAIT);

            return eventLoopGroup;
        } else {
            // Fallback to NIO
            return new NioEventLoopGroup(nThreads, threadFactory);
        }
    }

    public static Class<? extends ServerSocketChannel> getServerSocketChannelClass(EventLoopGroup eventLoopGroup) {
        if (eventLoopGroup instanceof EpollEventLoopGroup) {
            return EpollServerSocketChannel.class;
        } else {
            return NioServerSocketChannel.class;
        }
    }

    public static void enableTriggeredMode(ServerBootstrap bootstrap) {
        if (Epoll.isAvailable()) {
            bootstrap.childOption(EpollChannelOption.EPOLL_MODE, EpollMode.LEVEL_TRIGGERED);
        }
    }

    public static Class<? extends SocketChannel> getClientSocketChannelClass(EventLoopGroup eventLoopGroup) {
        if (eventLoopGroup instanceof EpollEventLoopGroup) {
            return EpollSocketChannel.class;
        } else {
            return NioSocketChannel.class;
        }
    }
}

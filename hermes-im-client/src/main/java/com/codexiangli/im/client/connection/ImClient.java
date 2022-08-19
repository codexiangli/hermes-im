package com.codexiangli.im.client.connection;

import com.codexiangli.im.client.config.ClientConfig;
import com.codexiangli.im.client.initializer.ImClientChannelInitializer;
import com.codexiangli.im.common.api.proto.*;
import com.codexiangli.im.common.util.netty.EventLoopUtil;
import com.google.protobuf.ByteString;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author lixiang
 * @since 2022/8/15
 */
@Slf4j
public class ImClient {

    /**
     * 客户端配置
     */
    private ClientConfig config;

    private final Bootstrap bootstrap;

    private final EventLoopGroup ioEventLoopGroup;

    private SocketChannel channel;

    /**
     * 重试次数
     */
    private int errorCount;

    public ImClient(ClientConfig config) {
        this.config = config;
        ioEventLoopGroup = EventLoopUtil.newEventLoopGroup(0, false,
                new DefaultThreadFactory("im-client"));
        bootstrap = new Bootstrap();
        bootstrap.group(ioEventLoopGroup)
                .channel(EventLoopUtil.getClientSocketChannelClass(ioEventLoopGroup))
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ImClientChannelInitializer());
    }

    public ChannelFuture connect() {
        ChannelFuture channelFuture = null;
        try {
            // todo 服务端发现
            channelFuture = bootstrap.connect(new InetSocketAddress("localhost", 8000)).sync();
        } catch (InterruptedException e) {
            errorCount++;
            if (errorCount >= config.getErrorCount()) {
                log.error("连接失败次数达到上限[{}]次", errorCount);
                // todo shutdown()
            }
            log.error("Connect fail!", e);
        }
        if (channelFuture.isSuccess()) {
            log.info("启动 im client 成功");
        }
        channel = (SocketChannel) channelFuture.channel();
        return channelFuture;
    }

    public void sendStringTo(String message) {
        Request request = Request.newBuilder()
                .setCmd(BaseCommand.newBuilder()
                        .setRequest(CommandRequest.newBuilder()
                                .setRequestId(System.currentTimeMillis())
                                .build())
                        .setType(BaseCommand.Type.MESSAGE))
                .setMetadata(RequestMetadata.newBuilder()
                        .addProperties(KeyValue.newBuilder().setKey("key1").setValue("value1").build())
                        .setSequenceId(System.currentTimeMillis())
                        .build())
                .setPayload(ByteString.copyFromUtf8(message)).build();
        channel.writeAndFlush(request);
    }

}

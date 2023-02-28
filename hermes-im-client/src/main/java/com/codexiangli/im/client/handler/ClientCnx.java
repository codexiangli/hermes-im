package com.codexiangli.im.client.handler;

import com.codexiangli.im.common.api.proto.CommandMessage;
import com.codexiangli.im.common.api.proto.Request;
import com.codexiangli.im.common.handler.ImHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author lixiang
 * @since 2022/8/15
 */
@Data
@Slf4j
public class ClientCnx extends ImHandler {

    private SocketAddress localAddress;
    private SocketAddress remoteAddress;

    private long counter = 1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.remoteAddress = ctx.channel().remoteAddress();
        this.ctx = ctx;
        // todo schedule job to keep alive
    }

    @Override
    protected void handleMessage(Request request) {
        String payload = request.getPayload().toString(StandardCharsets.UTF_8);
        log.info("客户端收到消息：{}, {}", payload, counter++);
    }

    @Override
    protected void handleMessagePush(Request request) {
        String message = request.getPayload().toString(StandardCharsets.UTF_8);
        log.info("客户端收到消息推送：{}, {}", message, counter++);
    }
}

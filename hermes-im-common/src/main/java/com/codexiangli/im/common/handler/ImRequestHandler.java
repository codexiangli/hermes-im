package com.codexiangli.im.common.handler;

import com.codexiangli.im.common.api.proto.BaseCommand;
import com.codexiangli.im.common.api.proto.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lixiang
 * @since 2022/8/15
 */
@Slf4j
public abstract class ImRequestHandler extends SimpleChannelInboundHandler<Request> {

    protected ChannelHandlerContext ctx;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        BaseCommand.Type type = request.getCmd().getType();
        switch (type) {
            case MESSAGE:
                handleMessage(request);
                break;
            case LOGIN:
                handleLogin(request);
                break;
            default:
                break;
        }
    }

    protected void handleMessage(Request request) {
        throw new UnsupportedOperationException();
    }

    protected void handleMessagePush(Request request) {
        throw new UnsupportedOperationException();
    }

    protected void handleLogin(Request request) {
        throw new UnsupportedOperationException();
    }
}

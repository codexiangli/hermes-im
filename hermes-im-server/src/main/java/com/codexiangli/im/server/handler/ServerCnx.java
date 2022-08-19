package com.codexiangli.im.server.handler;

import com.codexiangli.im.common.api.proto.*;
import com.codexiangli.im.common.handler.ImHandler;
import com.codexiangli.im.server.connection.Connection;
import com.codexiangli.im.server.connection.ConnectionHandler;
import com.codexiangli.im.server.connection.ConnectionPool;
import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author lixiang
 * @since 2022/8/15
 */
@Slf4j
public class ServerCnx extends ImHandler implements ConnectionHandler {

    private long counter = 1;

    public ServerCnx() {
        super();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        onConnectionOpen(new Connection(1L ,ctx.channel()));
        // todo schedule job to keep alive
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // todo
    }

    @Override
    protected void handleMessage(Request request) {
        String payload = request.getPayload().toString(StandardCharsets.UTF_8);
        log.info("服务器收到请求：{}, {}", payload, counter++);
        Request response = Request.newBuilder()
                .setCmd(BaseCommand.newBuilder()
                        .setRequest(CommandRequest.newBuilder()
                                .setRequestId(System.currentTimeMillis())
                                .build())
                        .setType(BaseCommand.Type.MESSAGE_RESPONSE))
                .setMetadata(RequestMetadata.newBuilder()
                        .addProperties(KeyValue.newBuilder().setKey("key1").setValue("value1").build())
                        .setSequenceId(System.currentTimeMillis())
                        .build())
                .setPayload(ByteString.copyFromUtf8(payload)).build();
        // todo 根据请求中的用户id找真正的connection 再进一步todo 封装推送任务() 再进一步 先推到pulsar/kafka
        Connection connection = ConnectionPool.getConnectionByUser(1L);
        connection.getChannel().writeAndFlush(response);
    }

    @Override
    public void onConnectionOpen(Connection connection) {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        ConnectionPool.createConnection(connection.getUserId(), connection);
    }

    @Override
    public void onConnectionClose(Connection connection) {

    }
}

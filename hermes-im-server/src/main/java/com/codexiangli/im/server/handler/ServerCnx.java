package com.codexiangli.im.server.handler;

import com.codexiangli.im.common.api.proto.CommandMessage;
import com.codexiangli.im.common.api.proto.Request;
import com.codexiangli.im.common.handler.ImHandler;
import com.codexiangli.im.core.connection.Connection;
import com.codexiangli.im.core.connection.ConnectionHandler;
import com.codexiangli.im.core.connection.ConnectionPool;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;

/**
 * @author lixiang
 * @since 2022/8/15
 */
@Slf4j
public class ServerCnx extends ImHandler implements ConnectionHandler {

    private long counter = 1;

    private PulsarClient pulsarClient;
    private Producer<Request> requestProducer;

    public ServerCnx(PulsarClient pulsarClient) {
        super();
        this.pulsarClient = pulsarClient;
        try {
            requestProducer = pulsarClient.newProducer(Schema.PROTOBUF(Request.class))
                    .topic("single-message-topic")
                    .create();
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        // todo schedule job to keep alive
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // todo
    }

    @Override
    protected void handleMessage(Request request) {
        // todo 根据请求中的用户id找真正的connection 再进一步todo 封装推送任务() 再进一步 先推到pulsar/kafka
        CommandMessage commandMessage = request.getCmd().getMsg();
        switch (commandMessage.getMessageType()) {
            case SINGLE:
            {
                try {
                    requestProducer.send(request);
                } catch (PulsarClientException e) {
                    e.printStackTrace();
                }
                break;
            }
            case GROUP:
                // todo
                break;
            default:
                break;
        }
    }

    @Override
    protected void handleLogin(Request request) {
        // todo 验证用户&密码
        onConnectionOpen(new Connection(request.getCmd().getLogin().getUserId() ,ctx.channel()));
    }

    @Override
    public void onConnectionOpen(Connection connection) {
        log.info("收到连接，userId：{}", connection.getUserId());
        ConnectionPool.createConnection(connection.getUserId(), connection);
    }

    @Override
    public void onConnectionClose(Connection connection) {

    }
}

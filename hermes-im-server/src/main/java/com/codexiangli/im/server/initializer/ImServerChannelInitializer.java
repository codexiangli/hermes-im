package com.codexiangli.im.server.initializer;

import com.codexiangli.im.common.api.proto.Request;
import com.codexiangli.im.core.dispatcher.routing.PulsarClientFactory;
import com.codexiangli.im.server.handler.ServerCnx;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lixiang
 * @since 2022/8/15
 */
@Slf4j
public class ImServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        /*ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
                Commands.DEFAULT_MAX_MESSAGE_SIZE + Commands.MESSAGE_SIZE_FRAME_PADDING, 0, 4, 0, 4));*/
        pipeline.addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(Request.getDefaultInstance()));
        ServerCnx cnx = new ServerCnx(PulsarClientFactory.createPulsarClient(null));
        pipeline.addLast("handler", cnx);
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
    }
}

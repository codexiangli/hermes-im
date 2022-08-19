package com.codexiangli.im.client.initializer;

import com.codexiangli.im.common.api.proto.Request;
import com.codexiangli.im.common.protocol.Commands;
import com.codexiangli.im.client.handler.ClientCnx;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
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
public class ImClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
       /* pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
                Commands.DEFAULT_MAX_MESSAGE_SIZE + Commands.MESSAGE_SIZE_FRAME_PADDING, 0, 4, 0, 4));*/
        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(Request.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        ClientCnx cnx = new ClientCnx();
        pipeline.addLast("handler", cnx);
    }
}

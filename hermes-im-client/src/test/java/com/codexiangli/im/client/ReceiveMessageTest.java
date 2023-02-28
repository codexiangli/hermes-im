package com.codexiangli.im.client;

import com.codexiangli.im.client.config.ClientConfig;
import com.codexiangli.im.client.connection.ImClient;
import io.netty.channel.ChannelFuture;

/**
 * @author lixiang
 * @since 2022/12/7
 */
public class ReceiveMessageTest {

    public static void main(String[] args) throws InterruptedException {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setErrorCount(3);
        ImClient imClient = new ImClient(clientConfig);
        ChannelFuture channelFuture = imClient.connect();
        imClient.login("002", "password");
        // 这里一定要加（至少在测试方法中一定要加，否则方法执行完TCP连接就断了）
        channelFuture.channel().closeFuture().sync();
    }
}

package com.codexiangli.im.client;

import com.codexiangli.im.client.config.ClientConfig;
import com.codexiangli.im.client.connection.ImClient;
import io.netty.channel.ChannelFuture;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author lixiang
 * @since 2022/8/15
 */
class ImClientTest {

    @Test
    void sendStringTo() throws InterruptedException {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setErrorCount(3);
        ImClient imClient = new ImClient(clientConfig);
        ChannelFuture channelFuture = imClient.connect();
        for (int i = 0; i < 1000; i++) {
            imClient.sendStringTo("hello world");
        }
        // 这里一定要加（至少在测试方法中一定要加，否则方法执行完TCP连接就断了）
        channelFuture.channel().closeFuture().sync();
    }
}
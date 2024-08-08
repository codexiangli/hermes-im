package com.codexiangli.im.client;

import com.codexiangli.im.client.config.ClientConfig;
import com.codexiangli.im.client.connection.ImClient;
import io.netty.channel.ChannelFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

/**
 * @author lixiang
 * @since 2022/8/15
 */
public class ImClientTest {

    @Test
    public void sendStringTo() throws InterruptedException {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setErrorCount(3);
        ImClient imClient = new ImClient(clientConfig);
        ChannelFuture channelFuture = imClient.connect();
        imClient.login("001", "password");
        for (int i = 0; i < 1000; i++) {
            imClient.sendStringTo("hello world");
        }
        // 这里一定要加（至少在测试方法中一定要加，否则方法执行完TCP连接就断了）
        channelFuture.channel().closeFuture().sync();
    }

    @Test
    public void receive() throws InterruptedException {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setErrorCount(3);
        ImClient imClient = new ImClient(clientConfig);
        ChannelFuture channelFuture = imClient.connect();
        imClient.login("002", "password");
        // 这里一定要加（至少在测试方法中一定要加，否则方法执行完TCP连接就断了）
        channelFuture.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setErrorCount(3);
        ImClient imClient = new ImClient(clientConfig);
        ChannelFuture channelFuture = imClient.connect();
        imClient.login("001", "password");
        for (int i = 0; i < 1000; i++) {
            imClient.sendStringTo("hello world");
        }
        // 这里一定要加（至少在测试方法中一定要加，否则方法执行完TCP连接就断了）
        channelFuture.channel().closeFuture().sync();
    }
}
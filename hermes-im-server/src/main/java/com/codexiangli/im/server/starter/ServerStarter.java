package com.codexiangli.im.server.starter;

import com.codexiangli.im.common.util.netty.EventLoopUtil;
import com.codexiangli.im.server.Server;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.io.IOException;

/**
 * @author lixiang
 * @since 2022/8/15
 */
public class ServerStarter {
    /*public static void main(String[] args) {
        // todo 支持配置
        Server server = new Server(EventLoopUtil.newEventLoopGroup(3, true,
                new DefaultThreadFactory("server-io")));
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}

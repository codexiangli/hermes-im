package com.codexiangli.im.server.spring;

import com.codexiangli.im.common.util.netty.EventLoopUtil;
import com.codexiangli.im.server.Server;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * @author lixiang
 * @since 2022/12/5
 */
@SpringBootApplication(scanBasePackages = {"com.codexiangli.im"})
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Server server = new Server(EventLoopUtil.newEventLoopGroup(3, true,
                new DefaultThreadFactory("server-io")));
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.codexiangli.im.server.connection;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * @author lixiang
 * @since 2022/8/19
 */
@Data
public class Connection {

    private Long userId;

    private Channel channel;

    public Connection(Long userId, Channel channel) {
        this.userId = userId;
        this.channel = channel;
    }
}

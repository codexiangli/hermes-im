package com.codexiangli.im.core.connection;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * @author lixiang
 * @since 2022/8/19
 */
@Data
public class Connection {

    private String userId;

    private Channel channel;

    public Connection(String userId, Channel channel) {
        this.userId = userId;
        this.channel = channel;
    }
}

package com.codexiangli.im.server.data;

import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * @author lixiang
 * @since 2022/8/16
 */
@Data
public class RequestPayload {

    private ByteBuf payload;
}

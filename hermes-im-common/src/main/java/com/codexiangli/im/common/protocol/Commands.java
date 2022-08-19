package com.codexiangli.im.common.protocol;

/**
 * @author lixiang
 * @since 2022/8/15
 */
public class Commands {

    public static final int DEFAULT_MAX_MESSAGE_SIZE = 5 * 1024 * 1024;
    public static final int MESSAGE_SIZE_FRAME_PADDING = 10 * 1024;

    /*public static RequestMetadata parseRequestMetadata(ByteBuf buffer) {
        RequestMetadata requestMetadata = new RequestMetadata();
        int metadataSize = (int) buffer.readUnsignedInt();
        requestMetadata.parseFrom(buffer, metadataSize);
        return requestMetadata;
    }*/
}

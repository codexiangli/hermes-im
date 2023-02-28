package com.codexiangli.im.core.connection;

/**
 * @author lixiang
 * @since 2022/8/19
 */
public interface ConnectionHandler {

    void onConnectionOpen(Connection connection);

    void onConnectionClose(Connection connection);
}

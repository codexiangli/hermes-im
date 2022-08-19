package com.codexiangli.im.server.connection;

/**
 * @author lixiang
 * @since 2022/8/19
 */
public interface ConnectionHandler {

    void onConnectionOpen(Connection connection);

    void onConnectionClose(Connection connection);
}

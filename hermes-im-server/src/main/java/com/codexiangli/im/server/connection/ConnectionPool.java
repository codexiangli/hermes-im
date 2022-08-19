package com.codexiangli.im.server.connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lixiang
 * @since 2022/8/19
 */
public class ConnectionPool {

    private static final Map<Long, Connection> pool = new ConcurrentHashMap<>();

    public static void createConnection(Long userId, Connection connection) {
        // todo 未来支持多客户端
        pool.putIfAbsent(userId, connection);
    }

    public static Connection getConnectionByUser(Long userId) {
        return pool.get(userId);
    }
}

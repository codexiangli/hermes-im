package com.codexiangli.im.core.connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lixiang
 * @since 2022/8/19
 */
public class ConnectionPool {

    private static final Map<String, Connection> pool = new ConcurrentHashMap<>();

    public static void createConnection(String userId, Connection connection) {
        // todo 未来支持多客户端
        pool.putIfAbsent(userId, connection);
    }

    public static Connection getConnectionByUser(String userId) {
        return pool.get(userId);
    }
}

package com.codexiangli.im.client;

/**
 * @author lixiang
 * @since 2022/8/19
 */
public interface UserClient {

    /**
     * 登陆
     *
     * @param userId
     * @param password
     */
    void login(String userId, String password);
}

package com.me.usercenter.constant;

/**
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/8/30 14:40
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 普通用户权限
     */
    String USER_ROLE = "user";

    /**
     * 管理员权限
     */
    String ADMIN_ROLE = "admin";

    /**
     * 违规用户
     */
    String USER_BAN_ROLE = "illegal";

    /**
     * 用户登录态信息失效时间
     */
    int USER_LOGIN_STATE_TIMEOUT = 1800;
}

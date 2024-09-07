package com.me.usercenter.common;

/**
 * 业务状态码
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/9/1 9:22
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
public enum ServiceStatusCode {
    SERVICE_SUCCESS(0, "Service succeed!", ""),
    REGISTER_ERROR(40000, "Register error!", ""),
    LOGIN_ERROR(40001, "Login error!", ""),
    LOGOUT_ERROR(40002, "Logout error!", ""),
    QUERY_ERROR(40002, "Query error!", ""),
    DELETE_ERROR(40003, "Delete error!", ""),
    CURRENT_USER_ERROR(40004, "Current User error!", ""),
    AUTH_ERROR(40101, "Auth error!", ""),
    SYSTEM_ERROR(50000, "Exceptions within the system!", "");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 状态码信息
     */
    private final String statusCodeInfo;
    /**
     * 状态码描述
     */
    private final String statusCodeDescription;

    ServiceStatusCode(int code, String statusCodeInfo, String statusCodeDescription) {
        this.code = code;
        this.statusCodeInfo = statusCodeInfo;
        this.statusCodeDescription = statusCodeDescription;
    }

    public int getCode() {
        return code;
    }

    public String getStatusCodeInfo() {
        return statusCodeInfo;
    }

    public String getStatusCodeDescription() {
        return statusCodeDescription;
    }
}

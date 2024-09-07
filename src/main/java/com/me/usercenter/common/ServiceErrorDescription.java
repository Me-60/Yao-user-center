package com.me.usercenter.common;

/**
 * 业务异常描述
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/9/1 11:53
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
public enum ServiceErrorDescription {
    NULL_ERROR("The parameter is null!"),
    NULL_PARAMETER_ERROR("The parameter of request is null!"),
    NULL_DATA_ERROR("The data of request is null"),
    FORMAT_ERROR("The username or password is in the wrong format!"),
    VERIFY_ERROR("The verification password is inconsistent with the password entered!"),
    REPEAT_ERROR("The username is exited!"),
    ENTER_ERROR("The username or password was entered incorrectly!"),
    NO_PERMISSIONS_ERROR("The permission of user is not enough!"),
    LOGOUT_ERROR("Fail to logout!"),
    NOT_LOGIN_ERROR("The user is not logged in!"),
    DATA_SAVE_ERROR("Failed to save data!"),
    ID_ERROR("The ID is incorrect!"),
    ILLEGAL_ERROR("The user is illegal!"),
    USER_ROLE_ERROR("The role of user is incorrectly!");

    private final String description;

    ServiceErrorDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

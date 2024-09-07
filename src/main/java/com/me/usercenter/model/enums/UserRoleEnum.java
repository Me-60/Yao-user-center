package com.me.usercenter.model.enums;

import org.apache.commons.lang3.ObjectUtils;

/**
 * 用户角色枚举
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/9/3 12:19
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
public enum UserRoleEnum {
    USER_ROLE("用户", "user"),
    ADMIN_ROLE("管理员", "admin"),
    ILLEGAL_ROLE("违规用户", "illegal");

    private String role;
    private String value;

    UserRoleEnum(String role, String value) {
        this.role = role;
        this.value = value;
    }

    public String getRole() {
        return role;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据角色值获取对应枚举
     *
     * @param value 角色值
     * @return 用户角色枚举
     */
    public static UserRoleEnum getEnumByValue(String value) {

        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (userRoleEnum.value.equals(value)) {
                return userRoleEnum;
            }
        }

        return null;
    }
}

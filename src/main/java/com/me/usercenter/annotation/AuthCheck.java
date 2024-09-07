package com.me.usercenter.annotation;

import com.me.usercenter.model.enums.UserRoleEnum;

import java.lang.annotation.*;

/**
 * 权限校验
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/9/3 9:54
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthCheck {

    /**
     * 用户角色
     *
     * @return
     */
    UserRoleEnum[] userRole() default UserRoleEnum.USER_ROLE;
}

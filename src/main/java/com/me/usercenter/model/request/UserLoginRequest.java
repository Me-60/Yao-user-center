package com.me.usercenter.model.request;

import com.me.usercenter.annotation.Printable;
import com.me.usercenter.common.Loggable;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/8/29 18:50
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
@Data
public class UserLoginRequest implements Serializable, Loggable {

    @Printable
    private String username;
    private String password;
}

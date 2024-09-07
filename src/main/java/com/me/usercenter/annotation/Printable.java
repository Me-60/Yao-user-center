package com.me.usercenter.annotation;

import java.lang.annotation.*;

/**
 * 可打印标记，便于 AOP 参数打印
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/9/3 16:30
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Printable {
}

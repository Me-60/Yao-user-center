package com.me.usercenter.utils;

import com.me.usercenter.common.BaseResponse;
import com.me.usercenter.common.ServiceStatusCode;
import com.me.usercenter.exception.BusinessException;

/**
 * 响应处理类
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/9/1 9:51
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
public class ResultUtils {

    public static <T> BaseResponse<T> serviceSuccess(T data) {

        return new BaseResponse<>(ServiceStatusCode.SERVICE_SUCCESS, data);
    }

    public static <T> BaseResponse<T> serviceError(BusinessException businessException) {

        return new BaseResponse<>(businessException);
    }

    public static <T> BaseResponse<T> systemError(RuntimeException runtimeException) {

        return new BaseResponse<>(runtimeException);
    }
}

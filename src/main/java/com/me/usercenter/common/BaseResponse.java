package com.me.usercenter.common;

import com.me.usercenter.exception.BusinessException;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/9/1 9:17
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 业务状态码
     */
    private int code;
    /**
     * 业务数据
     */
    private T data;
    /**
     * 业务执行消息
     */
    private String message;
    /**
     * 业务执行描述
     */
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ServiceStatusCode serviceStatusCode, T data) {
        this(serviceStatusCode.getCode(), data, serviceStatusCode.getStatusCodeInfo(), serviceStatusCode.getStatusCodeDescription());
    }

    public BaseResponse(BusinessException businessException) {
        this(businessException.getCode(), null, businessException.getMessage(), businessException.getDescription());
    }

    public BaseResponse(RuntimeException runtimeException) {
        this(ServiceStatusCode.SYSTEM_ERROR.getCode(), null, runtimeException.getMessage(), "");
    }
}

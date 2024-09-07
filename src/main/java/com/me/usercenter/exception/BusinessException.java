package com.me.usercenter.exception;

import com.me.usercenter.common.ServiceErrorDescription;
import com.me.usercenter.common.ServiceStatusCode;

/**
 * 自定义异常处理类
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/9/1 10:54
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
public class BusinessException extends RuntimeException {

    private int code;
    private String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ServiceStatusCode serviceStatusCode) {
        this(serviceStatusCode.getStatusCodeInfo(), serviceStatusCode.getCode(), serviceStatusCode.getStatusCodeDescription());
    }

    public BusinessException(ServiceStatusCode serviceStatusCode, String description) {
        this(serviceStatusCode.getStatusCodeInfo(), serviceStatusCode.getCode(), description);
    }

    public BusinessException(ServiceStatusCode serviceStatusCode, ServiceErrorDescription serviceErrorDescription) {
        this(serviceStatusCode.getStatusCodeInfo(), serviceStatusCode.getCode(), serviceErrorDescription.getDescription());
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

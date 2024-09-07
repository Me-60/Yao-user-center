package com.me.usercenter.exception;

import com.me.usercenter.common.BaseResponse;
import com.me.usercenter.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/9/1 23:34
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException businessException) {

        log.error("BusinessException: " + businessException.getMessage(), businessException);
        return ResultUtils.serviceError(businessException);
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException runtimeException) {

        log.error("RuntimeException: ", runtimeException);
        return ResultUtils.systemError(runtimeException);
    }
}

package com.me.usercenter.aop;

import com.me.usercenter.annotation.AuthCheck;
import com.me.usercenter.common.ServiceErrorDescription;
import com.me.usercenter.common.ServiceStatusCode;
import com.me.usercenter.exception.BusinessException;
import com.me.usercenter.model.dto.UserDTO;
import com.me.usercenter.model.enums.UserRoleEnum;
import com.me.usercenter.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验 AOP
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/9/3 10:19
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
@Aspect
@Order(2)
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doIntercept(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {

        UserRoleEnum[] requiredRoleEnum = authCheck.userRole();

        // 获取当前请求
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;

        HttpServletRequest request = attributes.getRequest();

        // 获取当前登录用户
        UserDTO user = userService.currentUser(request);

        // 校验权限
        if (requiredRoleEnum == null) {
            throw new BusinessException(ServiceStatusCode.SYSTEM_ERROR, ServiceErrorDescription.NULL_ERROR);
        }

        String userRole = user.getUserRole();

        if (UserRoleEnum.ILLEGAL_ROLE.getValue().equals(userRole)) {
            throw new BusinessException(ServiceStatusCode.AUTH_ERROR, ServiceErrorDescription.ILLEGAL_ERROR);
        }

        boolean checkRolePass = false;

        for (UserRoleEnum userRoleEnum : requiredRoleEnum) {

            if (userRoleEnum.getValue().equals(userRole)) {
                checkRolePass = true;
                break;
            }
        }

        if (!checkRolePass) {
            throw new BusinessException(ServiceStatusCode.AUTH_ERROR, ServiceErrorDescription.NO_PERMISSIONS_ERROR);
        }

        return joinPoint.proceed();
    }
}

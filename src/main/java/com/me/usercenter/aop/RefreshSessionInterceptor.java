package com.me.usercenter.aop;

import com.me.usercenter.constant.UserConstant;
import com.me.usercenter.model.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 刷新 session 有效期拦截器
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/9/6 15:47
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
@Aspect
@Order(1)
@Slf4j
@Component
public class RefreshSessionInterceptor {

    @Around("execution(* com.me.usercenter.controller.*.*(..))")
    public Object doIntercept(ProceedingJoinPoint joinPoint) throws Throwable {


        // 获取当前请求
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;

        HttpServletRequest request = attributes.getRequest();

        HttpSession session = request.getSession();

        Object attribute = session.getAttribute(UserConstant.USER_LOGIN_STATE);

        // 判断用户是否登录
        if (!(attribute instanceof UserDTO)) {
            log.info("用户未登录，不需要刷新有效期");
            // 用户未登录，不需要刷新有效期
            return joinPoint.proceed();
        }

        session.setMaxInactiveInterval(UserConstant.USER_LOGIN_STATE_TIMEOUT);

        return joinPoint.proceed();
    }
}

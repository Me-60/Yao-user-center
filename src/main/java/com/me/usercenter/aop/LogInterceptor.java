package com.me.usercenter.aop;

import com.me.usercenter.annotation.Printable;
import com.me.usercenter.common.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 请求响应日志 AOP
 *
 * @author Yao-happy
 * @version 0.0.1
 * 时间：2024/9/3 15:39
 * 作者博客：https://www.cnblogs.com/Yao-happy
 */
@Aspect
@Order(0)
@Slf4j
@Component
public class LogInterceptor {

    @Around("execution(* com.me.usercenter.controller.*.*(..))")
    public Object doIntercept(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        // 获取当前请求
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;

        HttpServletRequest request = attributes.getRequest();

        // 生成唯一的请求 ID
        String id = UUID.randomUUID().toString();

        // 获取请求路径
        String uri = request.getRequestURI();

        // 获取请求参数
        Object[] args = joinPoint.getArgs();
        ArrayList<Object> objects = new ArrayList<>();

        // 根据可打印注解进行信息筛选，提高信息安全
        // 声明一点就是，一般敏感信息都会在请求体中
        // 并且后端也会封装对应的类进行获取请求数据
        // 所以仅需处理封装类的可打印信息即可
        for (Object parameter : args) {

            if (parameter instanceof Loggable) {

                Class<?> parameterClass = parameter.getClass();

                Object parameterObject = parameterClass.newInstance();

                Field[] fields = parameterClass.getDeclaredFields();

                for (Field field : fields) {

                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Printable.class)) {
                        field.set(parameterObject, field.get(parameter));
                    }else {
                        Class<?> fieldType = field.getType();
                        Object fieldObject = fieldType.newInstance();
                        field.set(parameterObject, fieldObject);
                    }
                }

                objects.add(parameterObject);
            }
        }

        String requestParameter = "[" + StringUtils.join(objects, ",") + "]";

        // 打印请求日志
        log.info("Request start id: {}, path: {}, ip: {}, params: {}",
                id, uri, request.getRemoteHost(), requestParameter);

        // 执行请求
        Object result = joinPoint.proceed();

        stopWatch.stop();
        long execTime = stopWatch.getTime(TimeUnit.MILLISECONDS);

        // 打印响应日志
        log.info("Request end id: {}, execTime: {}",id, execTime);
        return result;
    }
}

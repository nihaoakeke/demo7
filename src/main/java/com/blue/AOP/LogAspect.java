package com.blue.AOP;

import com.blue.config.Log;
import com.sun.org.slf4j.internal.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

@Slf4j
@Aspect
@Component
public class LogAspect {

    //private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 定义@Before增强，拦截带有@Log注解的方法，并记录操作日志
     */
    @Before("@annotation(com.blue.config.Log)")
    public void before(JoinPoint joinPoint) throws Exception {
        // 获取目标方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取目标方法参数
        Object[] args = joinPoint.getArgs();
        // 获取目标方法所在类
        String className = joinPoint.getTarget().getClass().getName();

        // 获取Log注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);

        // 记录操作日志 实际开发中这里可以改为插入数据库
        log.info("方法:{}.{}, 参数:{}, 操作:{}", className, methodName, Arrays.toString(args), logAnnotation.operation());
    }

    @AfterReturning(value = "@annotation(com.blue.config.Log)", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) throws Exception {
        // 获取目标方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取目标方法参数
        Object[] args = joinPoint.getArgs();
        // 获取目标方法所在类
        String className = joinPoint.getTarget().getClass().getName();

        // 获取Log注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);

        // 获取方法返回结果是否成功
        boolean isSuccess = true;
        if (result != null && result instanceof Boolean) {
            isSuccess = (boolean) result;
        }

        // 记录操作日志及返回结果
        log.info("方法:{}.{}, 参数:{}, 操作:{}, 返回结果:{}",
                className, methodName, Arrays.toString(args), logAnnotation.operation(), isSuccess);
    }
}

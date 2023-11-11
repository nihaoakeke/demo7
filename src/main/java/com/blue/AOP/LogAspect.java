package com.blue.AOP;

import com.blue.config.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;


@Slf4j
@Aspect
@Component
public class LogAspect {
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

        boolean isSuccess = true;
        if (result != null && result instanceof Boolean) {
            isSuccess = (boolean) result;
        }

        log.info("方法:{}.{}, 参数:{}, 操作:{}, 返回结果:{}",
                className, methodName, Arrays.toString(args), logAnnotation.operation(), isSuccess);
    }
}

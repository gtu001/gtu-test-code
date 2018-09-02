package com.gtu.sysutil.aop;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAspect {

    private static Logger logger = Logger.getLogger(LoggingAspect.class);

    @Before("execution(* com.gtu.action..*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("logBefore() is running!");
        logger.info("hijacked : " + joinPoint.getSignature().getName());
        logger.info("******");
    }

    @After("execution(* com.gtu.action..*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("logAfter() is running!");
        logger.info("hijacked : " + joinPoint.getSignature().getName());
        logger.info("******");
    }

    @AfterReturning(pointcut = "execution(* com.gtu.action..*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("logAfterReturning() is running!");
        logger.info("hijacked : " + joinPoint.getSignature().getName());
        logger.info("Method returned value is : " + result);
        logger.info("******");
    }

    @AfterThrowing(pointcut = "execution(* com.gtu.action..*.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        logger.info("logAfterThrowing() is running!");
        logger.info("hijacked : " + joinPoint.getSignature().getName());
        logger.info("Exception : " + error);
        logger.info("******");
    }

    @Around("execution(* com.gtu.action..*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("logAround() is running!");
        logger.info("hijacked method : " + joinPoint.getSignature().getName());
        logger.info("hijacked arguments : " + Arrays.toString(joinPoint.getArgs()));
        logger.info("Around before is running!");
        Object result = joinPoint.proceed(); // continue on the intercepted method
        logger.info("Around after is running!");
        logger.info("******");
        return result;
    }
}
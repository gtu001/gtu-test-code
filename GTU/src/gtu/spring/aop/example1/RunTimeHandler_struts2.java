package gtu.spring.aop.example1;

import org.apache.commons.lang.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 统计方法执行时间的工具类,采用Spring AOP方式实现.
 * 
 * @author Kanine
 */
@Aspect
@Component
public class RunTimeHandler_struts2 {

    private static Logger logger = LoggerFactory.getLogger("code.coolbaby");

    @Pointcut("execution(public String *()) && !execution(public String toString())" + " && target(code.coolbaby.core.CRUDActionSupport)")
    void timer() {
    }

    @Around("timer()")
    public Object time(ProceedingJoinPoint joinPoint) throws Throwable {

        String clazz = joinPoint.getTarget().getClass().getSimpleName();
        String method = joinPoint.getSignature().getName();

        StopWatch clock = new StopWatch();
        clock.start();
        Object result = joinPoint.proceed();
        clock.stop();

        String[] params = new String[] { clazz, method, clock.getTime() + "" };
        logger.info("[{}]执行[{}]方法共消耗[{}]毫秒", params);

        return result;
    }
}
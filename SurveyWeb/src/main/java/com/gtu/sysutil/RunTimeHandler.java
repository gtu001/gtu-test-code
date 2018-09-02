package com.gtu.sysutil;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.apache.struts.actions.DispatchAction;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.stereotype.Service;

/**
 * 统计方法执行时间的拦截器,采用Spring AOP方式实现.
 * 
 * @author Kanine
 */
@Service("runTimeHandler")
public class RunTimeHandler implements MethodInterceptor {

    private static Logger logger = Logger.getLogger(RunTimeHandler.class);

    @SuppressWarnings("unchecked")
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Object[] args = methodInvocation.getArguments();
        String method = methodInvocation.getMethod().getName();
        String action = methodInvocation.getMethod().getDeclaringClass().getName();

        /**
         * 由于Spring使用了Cglib代理,导致不能直接取得目标类名,需作此转换
         */
        if (methodInvocation instanceof ReflectiveMethodInvocation) {
            Object proxy = ((ReflectiveMethodInvocation) methodInvocation).getProxy();
            action = StringUtils.substringBefore(proxy.toString(), "@");
            /**
             * 如使用了DispatchAction,将不能直接取得目标方法,需作此处理
             */
            if (proxy instanceof DispatchAction) {
                for (Object arg : args) {
                    if (arg instanceof HttpServletRequest) {
                        String newMethod = ((HttpServletRequest) arg).getParameter("method");
                        if (StringUtils.isNotBlank(newMethod)) {
                            method = newMethod;
                        }
                    }
                }
            }
        }

        /**
         * 方法参数类型，转换成简单类型
         */
        Class[] params = methodInvocation.getMethod().getParameterTypes();
        String[] simpleParams = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            simpleParams[i] = params[i].getSimpleName();
        }

        String simpleMethod = method + "(" + StringUtils.join(simpleParams, ",") + ")";

        logger.info(String.format("%s 开始执行[%s]方法", action, method));

        StopWatch clock = new StopWatch();
        clock.start();
        Object result = methodInvocation.proceed();
        clock.stop();

        logger.info(String.format("执行[%s]方法共消耗%s毫秒", simpleMethod, clock.getTime()));

        return result;
    }
}
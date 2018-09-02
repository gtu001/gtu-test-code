package gtu.spring.aop.example1;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
//import org.apache.struts.actions.DispatchAction;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

/**
 * <context:component-scan base-package="code.coolbaby"/> <aop:config>
 * <aop:advisor advice-ref="runTimeHandler"
 * pointcut="execution(* code.coolbaby.core.BaseDispatchAction.*(..))"/>
 * </aop:config>
 * 
 * 统计方法执行时间的拦截器,采用Spring AOP方式实现.
 * 
 * @author Kanine
 */
@Service("runTimeHandler")
public class RunTimeHandler implements MethodInterceptor {

    private static Logger logger = LoggerFactory.getLogger("code.coolbaby");

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
                    if (arg instanceof HttpServletRequest)
                        method = ((HttpServletRequest) arg).getParameter("method");
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

        logger.info("{} 开始执行[{}]方法", action, method);

        StopWatch clock = new StopWatch();
        clock.start();
        Object result = methodInvocation.proceed();
        clock.stop();

        logger.info("执行[{}]方法共消耗{}毫秒", simpleMethod, clock.getTime());

        return result;
    }
}
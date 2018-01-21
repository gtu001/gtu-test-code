package gtu.spring.aop;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterAdvice;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.aop.framework.ProxyFactory;

public class _TestBeforeAdvice {

    interface Waiter {
        void greetTo(String name);

        void serveTo(String name);

        void throwTest(String name) throws Exception;
    }

    static class NaiveWaiter implements Waiter {
        @Override
        public void greetTo(String name) {
            System.out.println("greet to " + name + " ...");
        }

        @Override
        public void serveTo(String name) {
            System.out.println("saving " + name + " ...");
        }

        @Override
        public void throwTest(String name) throws Exception {
            throw new Exception("error test...");
        }
    }

    static class GreetingBeforeAdvice implements MethodBeforeAdvice {
        //在目標類方法調用前執行
        @Override
        public void before(Method method, Object[] args, Object obj) throws Throwable {
            String clientName = (String) args[0];
            System.out.println("How are you! Mr. " + clientName + ".");
        }
    }

    static class GreetingAfterAdvice implements AfterReturningAdvice {
        @Override
        public void afterReturning(Object returnObj, Method method, Object[] args, Object obj) throws Throwable {
            System.out.println("Please enjoy youself!");
        }
    }

    static class GreetingInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            String clientName = (String) args[0];
            System.out.println("<X>How are you! Mr. " + clientName + ".");
            Object obj = invocation.proceed();
            System.out.println("<X>Please enjoy you self!");
            return obj;
        }
    }

    static class TransactionManager implements ThrowsAdvice {
        public void afterThrowing(Method method, Object[] args, Object target, Throwable ex) throws Throwable {
            System.out.println("[1]------------------------");
            System.out.println("method : " + method.getName());
            System.out.println("拋出異常 : " + ex.getMessage());
            System.out.println("成功回滾事務...");
        }

        public void afterThrowing(Throwable ex) throws Throwable {
            System.out.println("[2]------------------------");
            System.out.println("拋出異常 : " + ex.getMessage());
            System.out.println("成功回滾事務...");
        }
    }

    public static void main(String[] args) throws Exception {
        Waiter target = new NaiveWaiter();
        BeforeAdvice advice1 = new GreetingBeforeAdvice();
        AfterAdvice advice2 = new GreetingAfterAdvice();
        Advice advice3 = new GreetingInterceptor();
        ThrowsAdvice advice4 = new TransactionManager();
        //spring提供的代理工廠
        ProxyFactory pf = new ProxyFactory();
        //引介增強要通過創毽子類來生成代理
        pf.setProxyTargetClass(true);
        //設置代理目標
        pf.setTarget(target);
        //為代理目標添加增強
        pf.addAdvice(advice1);
        pf.addAdvice(advice2);
        pf.addAdvice(advice3);
        pf.addAdvice(advice4);
        //生成代理實例
        Waiter proxy = (Waiter) pf.getProxy();
        proxy.greetTo("John");
        proxy.serveTo("Tom");
        proxy.throwTest("");
    }
}

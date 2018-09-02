package gtu.spring.aop;

import gtu.spring.aop.StaticMethodMatcherPointcutAdvisorTest.Waiter;
import gtu.spring.aop._TestBeforeAdvice.GreetingBeforeAdvice;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;

public class DynamicPointcutAdvisorTest {

    static class GreetingDynamicPointcut extends DynamicMethodMatcherPointcut {
        static List<String> specialClientList = new ArrayList<String>();

        static {
            specialClientList.add("John");
            specialClientList.add("Tom");
        }

        public ClassFilter getClassFilter() {
            return new ClassFilter() {
                public boolean matches(Class<?> paramClass) {
                    System.out.println("調用getClassFilter()對" + paramClass.getSimpleName() + "做靜態檢查!");
                    return Waiter.class.isAssignableFrom(paramClass);
                }
            };
        }

        public boolean matches(Method paramMethod, Class<?> paramClass, Object[] paramArrayOfObject) {
            System.out.println("動態檢查:" + paramClass.getSimpleName() + "." + paramMethod.getName() + "!");
            String clientName = (String) paramArrayOfObject[0];
            return specialClientList.contains(clientName);
        }

        public boolean matches(Method paramMethod, Class<?> paramClass) {
            System.out.println("靜態檢查:" + paramClass.getSimpleName() + "." + paramMethod.getName() + "!");
            return "greetTo".equals(paramMethod.getName());
        }
    }

    public static void main(String[] args) {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(new GreetingDynamicPointcut());
        advisor.setAdvice(new GreetingBeforeAdvice());
        ProxyFactoryBean pf = new ProxyFactoryBean();
        pf.addAdvisor(advisor);
        pf.setTarget(new Waiter());
        pf.setProxyTargetClass(true);
        Waiter waiter = (Waiter) pf.getObject();
        waiter.serveTo("Peter");
        waiter.greetTo("Peter");
        waiter.serveTo("John");
        waiter.greetTo("John");
        System.out.println("done...");
    }
}

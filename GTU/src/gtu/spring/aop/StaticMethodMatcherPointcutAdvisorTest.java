package gtu.spring.aop;

import gtu.spring.aop._TestBeforeAdvice.GreetingBeforeAdvice;

import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

public class StaticMethodMatcherPointcutAdvisorTest {

    static class GreetingAdvisor extends StaticMethodMatcherPointcutAdvisor {
        private static final long serialVersionUID = 9175698644590142625L;

        @Override
        public boolean matches(Method paramMethod, Class<?> paramClass) {
            return "greetTo".equals(paramMethod.getName());
        }

        @Override
        public ClassFilter getClassFilter() {
            return new ClassFilter() {
                @Override
                public boolean matches(Class<?> paramClass) {
                    return Waiter.class.isAssignableFrom(paramClass);
                }
            };
        }
    }

    static class Waiter {
        public void greetTo(String name) {
            System.out.println("waiter greet to " + name + " ...");
        }

        public void serveTo(String name) {
            System.out.println("waiter serving to " + name + " ...");
        }
    }

    static class Seller {
        public void greetTo(String name) {
            System.out.println("seller greet to " + name + " ...");
        }
    }

    public static void main(String[] args) {
        GreetingAdvisor advisor = new GreetingAdvisor();
        GreetingBeforeAdvice advice = new GreetingBeforeAdvice();
        advisor.setAdvice(advice);
        Waiter waiter = new Waiter();
        Seller seller = new Seller();
        {
            ProxyFactoryBean pf = new ProxyFactoryBean();
            pf.setProxyTargetClass(true);
            pf.addAdvisor(advisor);
            pf.setTarget(waiter);
            Waiter waiter2 = (Waiter) pf.getObject();
            waiter2.greetTo("John");
            waiter2.serveTo("John");
        }
        {
            ProxyFactoryBean pf = new ProxyFactoryBean();
            pf.setProxyTargetClass(true);
            pf.addAdvisor(advisor);
            pf.setTarget(seller);
            Seller seller2 = (Seller) pf.getObject();
            seller2.greetTo("Mary");
        }
    }
}

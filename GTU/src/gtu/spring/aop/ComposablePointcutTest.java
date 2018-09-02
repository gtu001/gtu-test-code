package gtu.spring.aop;

import gtu.spring.aop.ControlFlowPointcutAdvisorTest.WaiterDelegate;
import gtu.spring.aop.StaticMethodMatcherPointcutAdvisorTest.Waiter;
import gtu.spring.aop._TestBeforeAdvice.GreetingBeforeAdvice;

import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.ControlFlowPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

public class ComposablePointcutTest {

    static Pointcut getComposablePointcut() {
        ComposablePointcut cp = new ComposablePointcut();

        Pointcut pt1 = new ControlFlowPointcut(WaiterDelegate.class, "service");

        NameMatchMethodPointcut pt2 = new NameMatchMethodPointcut();
        pt2.addMethodName("greetTo");

        cp.intersection(pt1);//交集
        cp.intersection((Pointcut) pt2);//交集
        return cp;
    }

    public static void main(String[] args) {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(getComposablePointcut());
        advisor.setAdvice(new GreetingBeforeAdvice());
        ProxyFactoryBean pf = new ProxyFactoryBean();
        pf.addAdvisor(advisor);
        pf.setTarget(new Waiter());
        pf.setProxyTargetClass(true);
        Waiter waiter = (Waiter) pf.getObject();
        WaiterDelegate wd = new WaiterDelegate();
        wd.setWaiter(waiter);
        waiter.serveTo("Peter1");
        waiter.greetTo("Peter2");
        wd.service("Peter3");
    }
}

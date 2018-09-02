package gtu.spring.aop;

import gtu.spring.aop.StaticMethodMatcherPointcutAdvisorTest.Seller;
import gtu.spring.aop.StaticMethodMatcherPointcutAdvisorTest.Waiter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RegexpMethodPointcutAdvisorTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("gtu/spring/aop/regexpMethodPointcutAdvisorTest.xml");
        Waiter waiter = (Waiter) context.getBean("waiter");
        Seller seller = (Seller) context.getBean("seller");
        waiter.greetTo("waiter1");
        waiter.serveTo("waiter2");
        seller.greetTo("seller");
        System.out.println("done...");
    }
}

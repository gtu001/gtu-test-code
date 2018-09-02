package gtu.spring.aop;

import gtu.spring.aop.StaticMethodMatcherPointcutAdvisorTest.Seller;
import gtu.spring.aop.StaticMethodMatcherPointcutAdvisorTest.Waiter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanNameAutoProxyCreatorTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/gtu/spring/aop/beanNameAutoProxyCreatorTest.xml");
        Waiter waiter = (Waiter) context.getBean("waiter");
        Seller seller = (Seller) context.getBean("seller");
        waiter.greetTo("Peter");
        waiter.serveTo("Peter");
        seller.greetTo("John");
        System.out.println("done...");
    }
}

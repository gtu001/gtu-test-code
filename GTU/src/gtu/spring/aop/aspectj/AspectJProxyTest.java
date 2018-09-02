package gtu.spring.aop.aspectj;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AspectJProxyTest {

    public static void main(String[] args) {
        AspectJProxyFactory factory = new AspectJProxyFactory();
        factory.setTarget(new Waiter());
        factory.setProxyTargetClass(true);
        factory.addAspect(PreGreetingAspect.class);
        Waiter waiter = factory.getProxy();
        waiter.greetTo("John");
        waiter.serveTo("John");
        ((Seller) waiter).sell("John", "A1");

        System.out.println("-------------------");
        ApplicationContext context = getContext("_aspectJProxyTest1.xml");
        Waiter waiter2 = (Waiter) context.getBean("waiter");
        waiter2.greetTo("Mary");
        waiter2.serveTo("Mary");
        ((Seller) waiter2).sell("Mary", "A2");

        System.out.println("-------------------");
        ApplicationContext context2 = getContext("_aspectJProxyTest2.xml");
        Waiter waiter3 = (Waiter) context2.getBean("waiter");
        waiter3.greetTo("Peter");
        waiter3.serveTo("Peter");
        ((Seller) waiter3).sell("Peter", "A3");
        System.out.println("done...");
    }

    static ApplicationContext getContext(String fileName) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/gtu/spring/aop/aspectj/" + fileName);
        return context;
    }

    @Aspect
    public static class PreGreetingAspect {
        //*  : 匹配任意字符,上下文中一個元素
        //.. : 匹配任意字符,上下文中多個元素 
        //+  : 表示按類型匹配指定類的所有類,必須跟在類明後面, Ex:com.baobaotao.Car+

        //切點函數
        //    方法切點函數
        //        execution() 方法匹配模式串
        //        @annotation() 方法註解類名 : 表示標示特定註解的方法連接點
        //    方法入參切點函數
        //        args() 類名 : 通過判別目標類方法運行時入參對象的類型定義指定連接點
        //        @args() 類型註解類名 : 通過判別目標方法運行時入參對象的類是否標註特定註解來指定連接點
        //    目標類切點函數
        //        within() 類名匹配串 : 表示特定域下的所有連接點. Ex:within(com.baobaotao.service.*)表示com.baobaotao.service包中所有連接點, within(com.baobaotao.service.*Service)表示包中所有以Service結尾的類的所有連接點
        //        target() 類名 : 假如目標類按類型匹配於指定類,則目標類的所有連接點匹配這個切點. Ex:如通過target(com.baobaotao.Waiter)定義的切點,Waiter以及實現類NaiveWaiter中所有連接點皆匹配該切點
        //        @within 類型註解類名 : 假如目標類按類型匹配於某個類A,且類A標註了特定註解,則目標類所有連接點皆匹配這個切點. Ex:@within(com.baobaotao.Monitorable)定義的切點,假如Waiter類標註了@Monitorable註解,則Waiter,NaiveWaiter類所有的連接點都匹配
        //        @target() 類型註解類名 : 假如目標類標註了特定註解,則目標類所有連接點皆匹配這個切點.
        //    代理類切點函數
        //        this() 類名 : 代理類案類型匹配於指定類,則被代理的目標類所有連接點匹配該切點.
        @Before("execution(* greetTo(..))")
        void beforeGreeting() {
            System.out.println("[Before]");
        }

        @AfterReturning("execution(* greetTo(..))")
        void afterReturningGreeting() {
            System.out.println("[AfterReturning]");
        }

        @Around("execution(* greetTo(..))")
        void aroundGreeting() {
            System.out.println("[Around]");
        }

        @AfterThrowing("execution(* greetTo(..))")
        void afterThrowingGreeting() {
            System.out.println("[AfterThrowing]");
        }

        @After("execution(* greetTo(..))")
        // 相當於finally
        void afterGreeting() {
            System.out.println("[After]");
        }

        @DeclareParents(value = "gtu.spring.aop.aspectj.AspectJProxyTest.Waiter", defaultImpl = SellerImpl.class)
        Seller seller;//引介增強
    }

    public static class SellerImpl implements Seller {
        @Override
        public void sell(String person1, String person2) {
            System.out.println("Seller : " + person1 + " sell Beer to " + person2);
        }
    }

    interface Seller {
        void sell(String person1, String person2);
    }

    static class Waiter {
        public void greetTo(String name) {
            System.out.println("waiter greet to " + name + " ...");
        }

        public void serveTo(String name) {
            System.out.println("waiter serving to " + name + " ...");
        }
    }
}

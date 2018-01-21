package gtu.spring.aop.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.ApplicationContext;

public class AspectJXmlConfig1Test {

    public static void main(String[] args) {
        ApplicationContext context = AspectJProxyTest.getContext("aspectJXmlConfigTest1.xml");
        NaiveWaiter waiter1 = (NaiveWaiter) context.getBean("naiveWaiter");
        NaughtyWaiter waiter2 = (NaughtyWaiter) context.getBean("naughtyWaiter");
        waiter1.greetTo("John");
        waiter2.greetTo("Sam");
        waiter1.sellBeer("Joe", 100);
        waiter1.serveTo("Johnson");
        waiter1.smile("Peter", 3);
        try {
            waiter1.checkBill(5);
        } catch (Exception ex) {
        }
        ((Seller) waiter1).sell("Bill", "Tom");
    }

    static class AdviceMethods {
        public void preGreeting() {
            System.out.println("-- preGreeting --");
        }

        public void postGreeting() {
            System.out.println("-- postGreeting --");
        }

        public void afterReturning(int retVal) {
            System.out.println("-- afterReturning : " + retVal);
        }

        public void aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
            System.out.println("-- aroundMethod start --");
            pjp.proceed();
            System.out.println("-- aroundMethod end --");
        }

        public void afterThrowingMethod(IllegalArgumentException iae) {
            System.out.println("-- afterThrowingMethod start --");
            System.out.println("exception : " + iae.getMessage());
            System.out.println("-- afterThrowingMethod end --");
        }

        public void bindParams(int num, String name) {
            //先名子匹配,後類型匹配
            System.out.println("-- bindParams start --");
            System.out.println("\t name : " + name);
            System.out.println("\t num : " + num);
            System.out.println("-- bindParams end --");
        }
    }

    interface Seller {
        void sell(String name1, String name2);
    }

    public static class SmartSeller implements Seller {
        @Override
        public void sell(String name1, String name2) {
            System.out.println("smart " + name1 + " sell beer to " + name2);
        }
    }

    static class NaiveWaiter {
        public void greetTo(String name) {
            System.out.println("naive waiter greet to " + name + " ...");
        }

        public void serveTo(String name) {
            System.out.println("waiter serving to " + name + " ...");
        }

        public void smile(String clientName, int times) {
            System.out.println("smile to " + clientName + " for " + times + " times.");
        }

        public int sellBeer(String name, int total) {
            System.out.println("sell beer to " + name + " , total : " + total);
            return total;
        }

        public void checkBill(int money) {
            if (money < 10) {
                throw new IllegalArgumentException("money less than 10..");
            }
        }
    }

    static class NaughtyWaiter {
        public void greetTo(String name) {
            System.out.println("naughty waiter greet to " + name + " ...");
        }
    }
}

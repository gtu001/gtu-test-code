package gtu.spring.aop.aspectj;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.context.ApplicationContext;

public class AspectJXmlConfig2Test {

    public static void main(String[] args) {
        ApplicationContext context = AspectJProxyTest.getContext("aspectJXmlConfigTest2.xml");
        NaiveWaiter waiter1 = (NaiveWaiter) context.getBean("naiveWaiter");
        waiter1.greetTo("John");
        waiter1.sellBeer("Joe", 100);
        waiter1.serveTo("Johnson");
        waiter1.smile("Peter", 3);
    }

    static class TestBeforeAdvice implements MethodBeforeAdvice {
        @Override
        public void before(Method method, Object[] args, Object target) throws Throwable {
            System.out.println("-- TestBeforeAdvice start --");
            System.out.println("args = " + Arrays.toString(args));
            System.out.println("-- TestBeforeAdvice end --");
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
}

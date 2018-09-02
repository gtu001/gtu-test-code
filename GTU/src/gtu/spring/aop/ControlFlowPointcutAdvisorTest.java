package gtu.spring.aop;

import gtu.spring.aop.StaticMethodMatcherPointcutAdvisorTest.Waiter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ControlFlowPointcutAdvisorTest {

    static class WaiterDelegate {
        private Waiter waiter;

        public void service(String clientName) {
            waiter.greetTo(clientName);
            waiter.serveTo(clientName);
        }

        public void setWaiter(Waiter waiter) {
            this.waiter = waiter;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/gtu/spring/aop/controlFlowPointcutAdvisorTest.xml");
        Waiter waiter = (Waiter) context.getBean("waiter");
        waiter.greetTo("Peter");
        waiter.serveTo("Peter");
        System.out.println("---------------");
        WaiterDelegate wd = new WaiterDelegate();
        wd.setWaiter(waiter);
        wd.service("Peter");
    }

}

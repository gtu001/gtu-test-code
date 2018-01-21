package gtu.spring.aop.aspectj;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

@Aspect
public class AspectJAnnotationTest {

    public static void main(String[] args) {
        AspectJProxyFactory factory = new AspectJProxyFactory();
        factory.setTarget(new NewWaiter());
        factory.setProxyTargetClass(true);
        factory.addAspect(AspectJAnnotationTest.class);
        NewWaiter waiter = factory.getProxy();
        waiter.greetTo("John");
        waiter.smile("Peter", 3);
        waiter.sellBeer("Joe", 100);
        try {
            waiter.checkBill(5);
        } catch (Exception ex) {
        }
    }

    static class TestNamePointcut {
        @Pointcut("within(gtu.spring.aop.aspectj.*)")
        private void inPackage() {
            System.out.println("#inPackage");
        }

        @Pointcut("execution(* greetTo(..))")
        protected void greetTo() {
            System.out.println("#greetTo");
        }

        @Pointcut("inPackage() and greetTo()")
        public void inPkgGreetTo() {
            System.out.println("#inPkgGreetTo");
        }
    }

    @Before("gtu.spring.aop.aspectj.TestAspect.TestNamePointcut.inPkgGreetTo()")
    public void pkgGreetTo() {
        System.out.println("## pkgGreetTo() executed!! ##");
    }

    @Around("execution(* greetTo(..)) && target(gtu.spring.aop.aspectj.TestAspect.NewWaiter)")
    public void joinPointAccess(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("---- joinPointAccess start ----");
        System.out.println("\t args : " + Arrays.toString(pjp.getArgs()));
        System.out.println("\t signature : " + pjp.getSignature());
        System.out.println("\t target : " + pjp.getTarget().getClass());
        System.out.println("\t kind : " + pjp.getKind());
        System.out.println("\t source location : " + pjp.getSourceLocation());
        System.out.println("\t static part : " + pjp.getStaticPart());
        System.out.println("\t this : " + pjp.getThis());
        Object result1 = pjp.proceed();
        System.out.println("1.result = " + result1);
        Object result2 = pjp.proceed(pjp.getArgs());
        System.out.println("2.result = " + result2);
        System.out.println("---- joinPointAccess end ----");
    }

    @Before("target(gtu.spring.aop.aspectj.TestAspect.NewWaiter) && args(name,num)")
    public void bindJoinPointParams(int num, String name) {
        //先名子匹配,後類型匹配
        System.out.println("-- bindJoinPointParams start --");
        System.out.println("\t name : " + name);
        System.out.println("\t num : " + num);
        System.out.println("-- bindJoinPointParams end --");
    }

    @Before("this(waiter)")
    public void bindProxyObj(NewWaiter waiter) {
        System.out.println("-- bindProxyObj start --");
        System.out.println(waiter.getClass().getName());
        System.out.println("-- bindProxyObj end --");
    }

    @Before("@within(monitor)")
    public void bindTypeAnnoObject(Monitorable monitor) {
        System.out.println("-- bindTypeAnnoObject start --");
        System.out.println(monitor.getClass().getName());//類的註解被代理了
        System.out.println("-- bindTypeAnnoObject end --");
    }

    @AfterReturning(value = "target(gtu.spring.aop.aspectj.TestAspect.NewWaiter)", returning = "retVal")
    public void bindReturnValue(int retVal) {
        System.out.println("-- bindReturnValue start --");
        System.out.println("return val = " + retVal);
        System.out.println("-- bindReturnValue end --");
    }

    @AfterThrowing(value = "target(gtu.spring.aop.aspectj.TestAspect.NewWaiter)", throwing = "iae")
    public void bindException(IllegalArgumentException iae) {
        System.out.println("-- bindException start --");
        System.out.println("exception : " + iae.getMessage());
        System.out.println("-- bindException end --");
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Monitorable {
    }

    @Monitorable
    static class NewWaiter {
        public void greetTo(String name) {
            System.out.println("waiter greet to " + name + " ...");
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

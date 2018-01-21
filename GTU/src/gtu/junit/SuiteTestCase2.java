package gtu.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * 可整批測試 第二種方法
 * 
 * @author Troy
 */
public class SuiteTestCase2 {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(MyTests1.class, Mytests2.class);
        for (Failure failure : result.getFailures()) {
            System.out.println("failure = " + failure.toString());
        }
    }

    public static class MyTests1 {

        @BeforeClass
        public static void beforeClass() {
            System.out.println(MyTests1.class.getSimpleName() + ".beforeClass");
        }

        @Before
        public void before() {
            System.out.println(this.getClass().getSimpleName() + ".before");
        }

        @Test
        public void test() {
            System.out.println(this.getClass().getSimpleName() + ".test");
        }

        @After
        public void after() {
            System.out.println(this.getClass().getSimpleName() + ".after");
        }

        @AfterClass
        public static void afterClass() {
            System.out.println(MyTests1.class.getSimpleName() + ".afterClass");
        }
    }

    public static class Mytests2 {

        @BeforeClass
        public static void beforeClass() {
            System.out.println(Mytests2.class.getSimpleName() + ".beforeClass");
        }

        @Before
        public void before() {
            System.out.println(this.getClass().getSimpleName() + ".before");
        }

        @Test
        public void test() {
            System.out.println(this.getClass().getSimpleName() + ".test");
        }

        @After
        public void after() {
            System.out.println(this.getClass().getSimpleName() + ".after");
        }

        @AfterClass
        public static void afterClass() {
            System.out.println(Mytests2.class.getSimpleName() + ".afterClass");
        }
    }
}

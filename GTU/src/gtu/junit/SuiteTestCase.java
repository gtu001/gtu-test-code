package gtu.junit;

import gtu.junit.SuiteTestCase.MyTests1;
import gtu.junit.SuiteTestCase.Mytests2;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 可整批測試
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MyTests1.class, Mytests2.class })
public class SuiteTestCase {

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

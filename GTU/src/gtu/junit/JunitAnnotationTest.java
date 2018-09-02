package gtu.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class JunitAnnotationTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.out.println("setUpBeforeClass...開始執行(只做一次)");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("tearDownAfterClass...執行(只做一次)");
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("setUp....每個test前執行");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("tearDown...每個test後執行");
    }

    @Test
    public void test1() {
        System.out.println("test1....");
    }

    @Test
    public void test2() {
        System.out.println("test2....");
    }

    @Test(expected = Exception.class)
    public void test3() throws Exception {
        System.out.println("test3....");
        throw new Exception();
    }

    @Test(timeout = 2000)
    public void test4() throws InterruptedException {
        System.out.println("test4....");
        while (true) {
            System.out.print("*");
            Thread.sleep(500);
        }
    }

    @Ignore
    @Test
    public void test5() {
        System.out.println("test5....");
    }

    @Test
    public void test6() {
        System.out.println("test6....");
        Assert.fail("String");// Let the method fail. Might be used to check
                              // that a certain part of the code is not reached.
                              // Or to have failing test before the test code is
                              // implemented.//
        Assert.assertTrue(true);//
        Assert.assertTrue(false);// Will always be true / false. Can be used to
                                 // predefine a test result, if the test is not
                                 // yet implemented.//
        Assert.assertTrue("[message]", true);// Checks that the boolean
                                             // condition is true.//
        // Assert.assertsEquals("[String message]", "expected",
        // "actual");//Tests that two values are the same. Note: for arrays the
        // reference is checked not the content of the arrays.//
        // Assert.assertsEquals("[String message]","expected", "actual",
        // "tolerance");//Test that float or double values match. The tolerance
        // is the number of decimals which must be the same.//
        Assert.assertNull("[message]", "object");// Checks that the object is
                                                 // null.//
        Assert.assertNotNull("[message]", "object");// Checks that the object is
                                                    // not null.//
        Assert.assertSame("[String]", "expected", "actual");// Checks that both
                                                            // variables refer
                                                            // to the same
                                                            // object.//
        Assert.assertNotSame("[String]", "expected", "actual");// Checks that
                                                               // both variables
                                                               // refer to
                                                               // different
                                                               // objects. //
    }
}

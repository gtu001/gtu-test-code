package gtu.apache;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.reflect.FieldUtils;

public class BeanUtilsTest {

    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        TestBean test = new TestBean();
        TestBean test2 = new TestBean();
        test2.test = (test);
//        System.out.println("name = " + BeanUtils.getProperty(test, "name"));
//        System.out.println("test.password = " + BeanUtils.getProperty(test2, "test.password"));
        
        System.out.println(FieldUtils.getDeclaredField(TestBean.class, "name"));
//        MethodUtils.invokeExactMethod(test2, "xxxxxxxxxx", new Object[]{"", 0});
        System.out.println("done...");
    }

    public static class TestBean {
        private String name;
        private String password;
        private void xxxxxxxxxx(String xxx, int yyy){
            System.out.println("xxxxxxxxx呼叫成功!!");
        }
        TestBean test;
    }
}

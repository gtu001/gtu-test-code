package gtu.log.finder;

import java.lang.reflect.Method;

public class MainTest {
    
    public static class HashMap {
    }

    public static void main(String[] args) {
        MainTest test = new MainTest();
        MyTest t = test.new MyTest();
        
        DebugMointerUI.startWithReflectAndDispose(test, t, new HashMap());
    }
    
    private class MyTest {
        public MyTest() {
        }
        
        private String test(String value) {
            System.out.println("[test]Orign : " + value);
            return "[test] rtn Orgin";
        }
        
        private String test2(String value) {
            System.out.println("[test2]Orign : " + value);
            return "[test2] rtn Orgin";
        }
    }

    private void execute(Object... value) {
        try {
            Class<?> clz = Class.forName("gtu.log.finder.DebugMointerUI");
            Method mth = clz.getDeclaredMethod("startWithReflectAndDispose", Object[].class);
            Object v = value;
            mth.invoke(clz, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

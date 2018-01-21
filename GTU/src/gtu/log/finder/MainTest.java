package gtu.log.finder;

import java.lang.reflect.Method;

public class MainTest {

    public static void main(String[] args) {
        MainTest test = new MainTest();

        MyTest t = test.new MyTest();
    }
    
    private class MyTest {
        public MyTest() {
            DebugMointerUI.startWithReflectAndDispose(this);
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

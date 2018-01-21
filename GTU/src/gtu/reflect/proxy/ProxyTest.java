package gtu.reflect.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class ProxyTest {

    public static void main(String[] args) {

        HandlerTest handler = new HandlerTest();
        handler.object = new TestInterfaceImpl();

        Object val = Proxy.newProxyInstance(ProxyTest.class.getClassLoader(), //
                new Class[] { TestInterface.class }, //
                handler);

        TestInterface testObj = (TestInterface) val;

        Object rtnVal = testObj.actionTest("this is message!");

        System.out.println("rtnVal = " + rtnVal);

        System.out.println("done...");
    }

    static class TestInterfaceImpl implements TestInterface {
        @Override
        public Object actionTest(String message) {
            System.out.println("==== body ==== message : " + message);
            return "return";
        }
    }

    static class HandlerTest implements InvocationHandler {

        private TestInterface object;

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println(" ---- invoke ---- .s");

            // System.out.println(" proxy = " + proxy); //會觸發遞迴錯誤 XXX
            System.out.println(" proxy = " + proxy.getClass());
            System.out.println(" method = " + method);
            System.out.println(" args = " + Arrays.toString(args));

            Object result = null;
            // result = method.invoke(proxy, args);//會觸發遞迴錯誤 XXX
            result = method.invoke(object, args);
            System.out.println(" ---- invoke ---- .e");
            return result;
        }
    }

    interface TestInterface {
        Object actionTest(String message);
    }
}

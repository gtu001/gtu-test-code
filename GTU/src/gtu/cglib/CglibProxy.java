package gtu.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibProxy implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public Object getProxy(Class<?> clz) {
        enhancer.setSuperclass(clz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("執行前!!");
        long during = System.currentTimeMillis();
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("執行後!!");
        during = System.currentTimeMillis() - during;
        System.out.println("耗時 : " + during);
        return result;
    }

    static class TestService {
        void test() {
            System.out.println("test......");
        }
    }

    public static void main(String[] args) {
        CglibProxy proxy = new CglibProxy();
        TestService service = (TestService) proxy.getProxy(TestService.class);
        service.test();
    }
}

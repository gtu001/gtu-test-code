package gtu.reflect.proxy.springproxy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExtensionPointProxyFactory_Main {
    
    private static final ApplicationContext _CONTEXT = new ClassPathXmlApplicationContext("gtu/reflect/proxy/springproxy/extensionPointProxyFactory_test.xml");

    public static void main(String[] args) {
        System.out.println(_CONTEXT);
        TestInterface bean = (TestInterface)_CONTEXT.getBean("epCounterCollectionHandler");
        bean.test();
        System.out.println("done...");
    }
    
    public static ApplicationContext getApplicationContext(){
        return _CONTEXT;
    }
    
    interface TestInterface {
        void test();
    }
    
    public static class TestInterfaceImpl implements TestInterface {
        @Override
        public void test() {
            System.out.println("AAAAAA");
        }
    }
}

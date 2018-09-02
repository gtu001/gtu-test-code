package gtu.reflect.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvocationHandlerTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        InvocationHandlerTest handler = new InvocationHandlerTest();
        DoSomeThingImpl doSomeThing = handler.new DoSomeThingImpl();

        LogHandler logHandler = handler.new LogHandler();
        Object object = logHandler.bind(doSomeThing);

        DoSomeThing doSome = (DoSomeThing) object;
        doSome.test();
    }

    interface DoSomeThing {
        void test();
    }

    class DoSomeThingImpl implements DoSomeThing {
        public void test() {
            System.out.println("DoSomeThing.test...");
        }
    }

    class LogHandler implements InvocationHandler {

        private Object delegate;

        private Logger log;

        public Object bind(Object delegate) {
            this.delegate = delegate;
            this.log = LoggerFactory.getLogger(delegate.getClass());
            return Proxy.newProxyInstance(delegate.getClass().getClassLoader(), delegate.getClass().getInterfaces(),
                    this);
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            log.debug("## " + delegate.getClass().getSimpleName() + "." + method.getName() + " start ...");
            result = method.invoke(delegate, args);
            log.debug("## " + delegate.getClass().getSimpleName() + "." + method.getName() + " end ...");
            return result;
        }
    }
}

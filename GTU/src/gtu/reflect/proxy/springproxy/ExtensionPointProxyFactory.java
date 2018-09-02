package gtu.reflect.proxy.springproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

public class ExtensionPointProxyFactory implements MethodInterceptor, InitializingBean, FactoryBean, BeanClassLoaderAware, InvocationHandler {
    
    private static final Logger logger = Logger.getLogger(ExtensionPointProxyFactory.class);
    
    private Class<?> serviceInterface;
    private String beanName;

    public ExtensionPointProxyFactory() {
    }

    /**
     * New Dyanamic Service Configuration with Simple Strategy type and
     * Ratetable Support
     *
     * @param serviceInterface
     * @param epName
     * @param strategyType
     * @param factorConfig
     * @return
     * @throws Exception
     */
    public static Object newInstance(String serviceInterface, String beanName) throws Exception {
        if (serviceInterface == null)
            return null;

        Class<?> serverInterface = Class.forName(serviceInterface);

        return Proxy.newProxyInstance(//
                ExtensionPointProxyFactory.class.getClassLoader(), //
                new Class[] { serverInterface }, //
                new ExtensionPointProxyFactory() //
                );
    }

    /**
     * Invoke a dynamic service
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.debug("#. invoke .s");
        logger.debug("beanName = " + beanName);
        logger.debug("context = " + getContext());
        if (beanName != null) {
            Object impl = null;
            try {
                impl = getContext().getBean(beanName);
                logger.debug("impl = " + impl);
            } catch (NoSuchBeanDefinitionException e) {
                throw new Exception("Can not find bean '" + beanName + "' defined!", e);
            }
            return method.invoke(impl, args);
        }

        throw new Exception("Spring Bean Name is NULL!");
    }
    
    private ApplicationContext getContext(){
        return ExtensionPointProxyFactory_Main.getApplicationContext();
    }

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    private Object serviceProxy;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        return this.invoke(null, invocation.getMethod(), invocation.getArguments());
    }

    public void afterPropertiesSet() throws Exception {
        this.serviceProxy = new ProxyFactory(getObjectType(), this).getProxy(this.beanClassLoader);
    }

    public Object getObject() {
        return this.serviceProxy;
    }

    public Class<?> getObjectType() {
        return serviceInterface;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }
    
    public void setServiceInterface(String classPath) throws ClassNotFoundException{
        serviceInterface = Class.forName(classPath);
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}

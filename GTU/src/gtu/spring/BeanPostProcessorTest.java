package gtu.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanPostProcessorTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("gtu/spring/BeanPostProcessorTest.xml");
        System.out.println("done...");
    }

    private static Logger log = LoggerFactory.getLogger(BeanPostProcessorTest.class);

    static class Car {
        private String brand;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }
    }

    static class MyBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.debug("#### {} : {}", beanName, bean);
            return bean;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            log.debug("#### {} : {}", beanName, bean);
            return bean;
        }
    }

    static class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) throws BeansException {
            BeanDefinition bd = bf.getBeanDefinition("car");
            bd.getPropertyValues().addPropertyValue("brand", "奇瑞QQ");
            log.debug("####  postProcessBeanFactory ..");
        }
    }
}

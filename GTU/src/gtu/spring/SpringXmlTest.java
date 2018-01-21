package gtu.spring;

import gtu.db.derby.SpringDerbyTestUtil;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author Troy 2009/02/02
 * 
 */
public class SpringXmlTest {

    // public static void main(String[] args) {
    // }

    // @Test
    public void testAnnotationConfigApplicationContext() {
        System.out.println("# testAnnotationConfigApplicationContext ...");

        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);

        System.out.println("result = " + context.getBean("testBean"));
        System.out.println("done...");
    }

    // 只能用 public static 其他inner 或匿名都不成功
    @Configuration
    public static class TestConfiguration {

        @Bean(name = "testBean")
        // method name 隨意 , 不可為private, static, final
        public String test() {
            return "hello world";
        }
    }

    public void testClassPathRecourceBeanFactory() {
        System.out.println("# testClassPathRecourceBeanFactory ...");
        Resource resource = new ClassPathResource("bean-derby.xml", SpringDerbyTestUtil.class);
        BeanFactory beanFactory = new XmlBeanFactory(resource);
        System.out.println(beanFactory);
    }

    public void testFileSystemResouceBeanFactory() {
        System.out.println("# testFileSystemResouceBeanFactory ...");
        Resource resource = new FileSystemResource("src/resource/applicationContext.xml");
        BeanFactory beanFactory = new XmlBeanFactory(resource);
        System.out.println(beanFactory);
    }

    /**
     * 指定檔案路徑
     */
    public void testFileSystemXml() {
        System.out.println("# testFileSystemXml ...");
        ApplicationContext context2 = new FileSystemXmlApplicationContext("src/resource/applicationContext.xml");
        System.out.println(context2);
    }

    /**
     * 指定class所在package
     */
    public void testClassPathXml() {
        System.out.println("# testClassPathXml ...");
        ApplicationContext context1 = new ClassPathXmlApplicationContext("bean-derby.xml", SpringDerbyTestUtil.class);
        System.out.println(context1);
    }
}

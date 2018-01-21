package gtu.mybatis.example;

import gtu.mybatis.example.domain.Category;
import gtu.mybatis.example.service.CatalogService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml", MainTest.class);

        for (String beanName : context.getBeanDefinitionNames()) {
            System.out.println("bean = " + beanName);
        }

        CatalogService service = (CatalogService) context.getBean("catalogService");
        for (Category category : service.getCategoryList()) {
            System.out.println("category = " + category);
        }
        System.out.println("done...");
    }
}

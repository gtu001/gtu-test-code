package gtu.jotm.hibernate;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/gtu/jotm/hibernate/jotm_context.xml");
        System.out.println("done...");
    }
}

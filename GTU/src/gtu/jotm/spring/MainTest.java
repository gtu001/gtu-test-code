package gtu.jotm.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/gtu/jotm/spring/jotm_context.xml");
        MyServiceImpl ms = (MyServiceImpl) context.getBean("myService");
        ms.addGrade();
        System.out.println("done...");
    }

}

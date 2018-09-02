package gtu.spring.aop.classconfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class _MainTest {

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);

        bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        
        System.out.println("done...");
    }
}

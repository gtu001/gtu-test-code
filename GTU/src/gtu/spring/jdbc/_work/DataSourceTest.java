package gtu.spring.jdbc._work;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DataSourceTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("gtu/spring/jdbc/_work/dataSource_test.xml");
        DataSource dataSource = (DataSource) context.getBean("dataSource");
        System.out.println("done...");
    }

}

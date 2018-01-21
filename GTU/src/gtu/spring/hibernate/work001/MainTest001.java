package gtu.spring.hibernate.work001;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest001 {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:gtu/spring/hibernate/work001/springHibernateConfig.xml");
        SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");

        Session session = sessionFactory.openSession();

        SQLQuery sqlQuery = session.createSQLQuery("select * from user_info where user_id in (:user_id_array) ");
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        sqlQuery.setParameterList("user_id_array", Arrays.asList("gtu001", "test001", "aaa001", "liu001", "li001", "chou001"));
        List<Map<String, Object>> list = sqlQuery.list();
        for (Map<String, Object> m : list) {
            System.out.println(m);
        }
        System.out.println("done...");
    }
}

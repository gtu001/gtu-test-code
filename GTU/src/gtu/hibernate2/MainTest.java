package gtu.hibernate2;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

public class MainTest {

    public static void main(String[] args) {
        Session session = HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();

        User user = new User();
        UserId userId = new UserId();
        user.setId(userId);
        userId.setUserId("test1");
        userId.setUserName("test2");
        userId.setUserPassword("test3");
        //        session.save(user);
        tx.commit();

        {
            Criteria crit = session.createCriteria(User.class);
            crit.add(Expression.idEq(userId));
            crit.add(Expression.ne("id.userId", "user_id"));
            crit.add(Expression.eq("id.userId", "user_id"));

            crit.add(Restrictions.gt("id.userId", new Double(1.0)));
            crit.add(Restrictions.like("id.userId", "P%"));
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            Criterion price = Restrictions.gt("id.userId", new Double(25.0));
            User user2 = (User) crit.uniqueResult();

            crit.setMaxResults(100);
            crit.setFirstResult(1);
            List<?> results = crit.list();
        }

        HibernateUtil.closeSession();
        HibernateUtil.sessionFactory.close();

        System.out.println("done...");
    }
}

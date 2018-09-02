package gtu.spring.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class HibernateTemplateTest {
    static class BaseDao {
        @Autowired
        private HibernateTemplate hibernateTempate;

        public HibernateTemplate getHibernateTempate() {
            return hibernateTempate;
        }
    }

    static class Forum {
    }

    static class ForumHibernateDao extends BaseDao {
        public void addForum(Forum forum) {
            getHibernateTempate().save(forum);
        }

        public void updateForum(Forum forum) {
            getHibernateTempate().update(forum);
        }

        public void getForum(int forumId) {
            getHibernateTempate().get(Forum.class, forumId);
        }

        @SuppressWarnings("unchecked")
        public List<Forum> findForumByName(String forumName) {
            return (List<Forum>) getHibernateTempate().find("from Forum f where f.forumName like ?", forumName + "%");
        }

        public long getForumNum() {
            Object obj = getHibernateTempate()//
                    .iterate("select count(f.forumId) from Forum f")//
                    .next();
            return (Long) obj;
        }

        public long getForumNum2() {
            Long forumNum = getHibernateTempate().execute(new HibernateCallback<Long>() {
                @Override
                public Long doInHibernate(Session session) throws HibernateException, SQLException {
                    Object obj = session//
                            .createQuery("select count(f.forumId) from Forum f")//
                            .list()//
                            .iterator()//
                            .next();//
                    return (Long) obj;
                }
            });
            return forumNum;
        }
    }
}

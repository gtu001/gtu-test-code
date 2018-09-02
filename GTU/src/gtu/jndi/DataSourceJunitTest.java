package gtu.jndi;

import java.util.Hashtable;
import java.util.List;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

public class DataSourceJunitTest {

    static Logger log = LoggerFactory.getLogger(DataSourceJunitTest.class);

    @Test
    public void testDataSource() throws NamingException {
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        builder.bind("jdbc/chun0000", getDataSource());
        SessionFactory sessionFactory = new Configuration().configure("/gtu/jndi/hibernate.cfg.xml").buildSessionFactory();
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from User t where t.id.userId = ?");
        query.setString(0, "user_id");
        List<?> list = query.list();
        log.debug(">>>>" + list);
        sessionFactory.close();
        log.debug("done....");
    }

    //@Test
    public void testDataSource1() throws NamingException {
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        InitialContextFactory factory = builder.createInitialContextFactory(null);
        builder.bind("jdbc/chun0000", getDataSource());
        Context context = factory.getInitialContext(null);
        System.out.println("### " + context.lookup("jdbc/chun0000"));
        log.debug("done....");
    }

    //@Test
    public void testDataSource3() throws NamingException {
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        InitialContextFactory factory = builder.createInitialContextFactory(null);
        Context context = factory.getInitialContext(null);
        Object obj = new Object();
        context.bind("testObj", obj);
        context.rebind("testInteger", new Integer(0));
        context.rename("testInteger", "zero");
        context.bind("testStr", new String("testStr"));
        log.debug("testStr =>" + context.lookup("testStr"));
        context.unbind("testStr");

        Hashtable env2 = new Hashtable();
        env2.put("key1", "value1");
        context = factory.getInitialContext(env2);
        context.addToEnvironment("key2", "value2");
        context.removeFromEnvironment("key1");
        log.debug("key1 =>" + context.getEnvironment().get("key1"));
        log.debug("testObj =>" + (context.lookup("testObj") == obj));

        NamingEnumeration<Binding> enu = context.listBindings("");
        while (enu.hasMoreElements()) {
            Binding binding = enu.nextElement();
            log.debug("" + binding.getName() + " ... " + binding.getObject());
        }

        NamingEnumeration<NameClassPair> pairEnum = context.list("");
        while (pairEnum.hasMore()) {
            NameClassPair pair = (NameClassPair) pairEnum.next();
            log.debug("" + pair.getName() + " ... " + pair.getClassName());
        }
        log.debug("done....");
    }

    DataSource getDataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl("jdbc:hsqldb:file:db/hsql/testdb;shutdown=true");
        driverManagerDataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        driverManagerDataSource.setUsername("SA");
        driverManagerDataSource.setPassword("");
        return driverManagerDataSource;
    }
}

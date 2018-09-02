package gtu.jotm.simple;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.enhydra.jdbc.standard.StandardXADataSource;
import org.objectweb.jotm.Jotm;
import org.springframework.jdbc.core.JdbcTemplate;

public class SimpleJotmTest {

    public static void main(String[] args) throws NamingException, SQLException, NotSupportedException, SystemException {
        //创建JTA的UserTransaction和TransactionManager。
        //下面会使用UserTransaction进行事务的提交和回滚。
        //TransactionManager用来管理事务源。
        Jotm jotm = new Jotm(true, false);
        TransactionManager transactionManager = jotm.getTransactionManager();
        UserTransaction utx = jotm.getUserTransaction();

        //创建一个分布式数据源 XADataSource
        StandardXADataSource dataSource1 = new StandardXADataSource();
        dataSource1.setDriverName("org.hsqldb.jdbcDriver");
        dataSource1.setUser("sa");
        dataSource1.setUrl("jdbc:hsqldb:file:db/hsql/testdb;shutdown=true");
        //将该数据源加入到TransactionManager管理范围内
        dataSource1.setTransactionManager(transactionManager);

        //得到两个分布式Connection
        Connection cn1 = dataSource1.getXAConnection().getConnection();

        utx.begin();
        try {
            cn1.createStatement().execute("UPDATE USER SET user_password='1234' WHERE user_id = 'user_id'");
            //模拟抛出一个业务异常
            int a = 1 / 0;
            utx.commit();
        } catch (Exception e) {
            utx.rollback();
            e.printStackTrace();
        }

        System.out.println(new JdbcTemplate(dataSource1).queryForMap("SELECT * FROM USER WHERE user_id = 'user_id'"));
        System.out.println("done...");
    }
}

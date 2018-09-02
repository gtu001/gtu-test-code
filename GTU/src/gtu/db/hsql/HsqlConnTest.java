package gtu.db.hsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class HsqlConnTest {

    private Connection conn;

    //DRIVER
    //    org.hsqldb.jdbc.JDBCDriver
    //    org.hsqldb.jdbcDriver
    //    org.hibernate.dialect.HSQLDialect

    @Test
    public void testFileConn() throws SQLException {
        System.out.println("# testFileConn ...");
        // 會在user.dir底下建立 /db/hsql 的目錄底下建立 testdb
        conn = DriverManager.getConnection("jdbc:hsqldb:file:db/hsql/testdb;shutdown=true", "SA", "");
    }

    public void testFileConn1() throws SQLException {
        System.out.println("# testFileConn1 ...");
        // "~/" 開頭會被替換成 user.home
        conn = DriverManager.getConnection("jdbc:hsqldb:file:~/db/hsql/testdb;shutdown=true", "SA", "");
    }

    @Test
    public void testFileConn2() throws SQLException {
        System.out.println("# testFileConn2 ...");
        conn = DriverManager.getConnection("jdbc:hsqldb:file:${user.dir}/xxx/db/hsql/testdb;shutdown=true", "SA", "");
    }

    @Test
    public void testHttpConn() throws SQLException {
        System.out.println("# testHttpConn ...");
        conn = DriverManager.getConnection("jdbc:hsqldb:http://localhost/xdb;shutdown=true", "SA", "");
    }

    @Test
    public void testMemeryConn() throws SQLException {
        System.out.println("# testMemeryConn ...");
        conn = DriverManager.getConnection("jdbc:hsqldb:mem:testdb;shutdown=true", "SA", "");
    }

    @After
    public void after() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

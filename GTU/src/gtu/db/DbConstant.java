package gtu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DbConstant {

    public static void main(String[] args) {
        DataSource ds = getDerbyDataSource();
        Connection conn = null;
        try {
            conn = ds.getConnection();
            System.out.println("test fine ...");

            int v = Integer.parseInt("01");
            System.out.println(v);
            System.out.println("done...");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void applyToConnectionPool(BasicDataSource bds) {
        bds.setMaxActive(10);
        bds.setInitialSize(1);
        bds.setMaxIdle(600000);
    }

    public static Connection getTestConnection() throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        String url = "jdbc:oracle:thin:@172.16.7.189:1524:c189u1";
        String user = "tgl_main_dev_o12";
        String pwd = "tgl_main_dev_o12pwd";
        Connection conn = DriverManager.getConnection(url, user, pwd);
        return conn;
    }

    public static DataSource getTestDataSource() {
        BasicDataSource bds = new BasicDataSource();
        // bds.setUrl("jdbc:mysql://localhost:3306/test");
        bds.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8");
        bds.setUsername("sa");
        bds.setPassword("");
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        applyToConnectionPool(bds);
        return bds;
    }

    public static DataSource getTestDataSource_Oracle() {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=DBDAO)))");
        bds.setUsername("scott");
        bds.setPassword("tiger");
        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        applyToConnectionPool(bds);
        return bds;
    }

    public static DataSource getTestDataSource_R5DEV() {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:oracle:thin:@10.67.67.86:1521:ebosit1");
        bds.setUsername("ls_sit_dev");
        bds.setPassword("ls_sit_devpwd");
        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        applyToConnectionPool(bds);
        return bds;
    }

    public static DataSource getTestDataSource_SIT3() {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:oracle:thin:@ploodse-scan.transglobe.com.tw:1521/ebaosit3");
        bds.setUsername("ls_sit3_read");
        bds.setPassword("ls_sit3_read_only");
        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        applyToConnectionPool(bds);
        return bds;
    }

    public static DataSource getTestDataSource_R5UAT() {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:oracle:thin:@10.67.67.70:1521:ebouat2");
        bds.setUsername("ls_uat_read");
        bds.setPassword("ls_uat_read_only");
        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        applyToConnectionPool(bds);
        return bds;
    }

    public static DataSource getTestDataSource_Oracle_HP() {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=60.250.95.121)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=ORCL)))");
        bds.setUsername("c##sysadm");
        bds.setPassword("123456");
        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        applyToConnectionPool(bds);
        return bds;
    }

    public static DataSource getTestDataSource_Oracle_HP_new() {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=122.116.167.154)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=IBTDCS1)))");
        bds.setUsername("sysadm");
        bds.setPassword("123456");
        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        applyToConnectionPool(bds);
        return bds;
    }

    public static DataSource getTestDataSource_Oracle_EGD() {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:oracle:thin:@10.67.67.6:1521:tgdev1");
        bds.setUsername("tptest04");
        bds.setPassword("tptest04pwd");
        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        applyToConnectionPool(bds);
        return bds;
    }

    public static DataSource getTestDataSource_Fuco() {
        BasicDataSource ds2 = new BasicDataSource();
        ds2.setUrl("jdbc:sqlserver://192.168.93.205\\SQLEXPRESS;DatabaseName=SCSB_CCBILL");
        ds2.setUsername("sa");
        ds2.setPassword("12345678");
        ds2.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        applyToConnectionPool(ds2);
        return ds2;
    }

    public static DataSource getTestDataSource_Fuco_ESUN() {
        BasicDataSource ds2 = new BasicDataSource();
        ds2.setUrl("jdbc:sqlserver://192.168.93.205\\SQLEXPRESS;DatabaseName=ESUN_CCBILL");
        ds2.setUsername("sa");
        ds2.setPassword("12345678");
        ds2.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        applyToConnectionPool(ds2);
        return ds2;
    }

    public static DataSource getTestDataSource_FucoOracle() {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:oracle:thin:@192.168.93.215:1521:SCSB");
        bds.setUsername("henry");
        bds.setPassword("Fuco1234");
        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        applyToConnectionPool(bds);
        return bds;
    }

    public static DataSource getTestDataSource_FucoOracle_SCSB() {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:oracle:thin:@10.10.2.140:1521:rd11g");
        bds.setUsername("ccbilldb");
        bds.setPassword("Cc1234@!");
        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        applyToConnectionPool(bds);
        return bds;
    }

    public static DataSource getSpringDataSource() {
        DriverManagerDataSource bds = new DriverManagerDataSource();
        bds.setUrl("jdbc:oracle:thin:@192.168.93.215:1521:SCSB");
        bds.setUsername("henry");
        bds.setPassword("Fuco1234");
        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        return bds;
    }

    public static DataSource getDerbyDataSource() {
        DriverManagerDataSource bds = new DriverManagerDataSource();
        bds.setUrl("jdbc:derby://localhost:1527/seconddb;create=true");
        bds.setUsername("test");
        bds.setPassword("1234");
        bds.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
        return bds;
    }

    public static DataSource getPostgresDataSource() {
        DriverManagerDataSource bds = new DriverManagerDataSource();
        bds.setUrl("jdbc:postgresql://192.168.99.100:5432/test");
        bds.setUsername("test");
        bds.setPassword("1234");
        bds.setDriverClassName("org.postgresql.Driver");
        return bds;
    }

    public static Connection getTestConnection_CTBC() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@172.24.36.77:1521:XE", "ebmwdev", "devebmw");
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getHsqlConn() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:d:/testDB;shutdown=true", "SA", "");
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getMysqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8", "sa", "");
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getDB2Connection() {
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:db2://10.0.75.1:50000/test:retrieveMessagesFromServerOnGetMessage=true;currentSchema=test2", "db2inst1", "db2inst1-pwd");

            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getDB2Connection_FTP() {
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:db2://10.1.249.35:50000/ampm_63", "ampm", "ampm63");
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getDB2Connection_FTP2() {
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:db2://10.1.2.58:60004/ampm", "ampm", "ampm58");
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final String DRIVER_HSQL = "org.hsqldb.jdbcDriver";
    public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    public static final String DRIVER_MSSQL = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
    public static final String DRIVER_DB2 = "com.ibm.db2.jcc.DB2Driver";
    public static final String DRIVER_DERBY = "org.apache.derby.jdbc.ClientDriver";
    public static final String DRIVER_POSTGRES = "org.postgresql.Driver";
}

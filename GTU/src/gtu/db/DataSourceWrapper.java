package gtu.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class DataSourceWrapper implements DataSource {

    private String dbUrl;
    private static final Logger logger = Logger.getLogger(DataSourceWrapper.class);

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        String url = "jdbc:oracle:thin:@172.16.7.189:1524:c189u1"; 
        String user = "tgl_main_dev_o12"; 
        String pwd = "tgl_main_dev_o12pwd";
        Connection conn = DriverManager.getConnection(url, user, pwd);
        logConnectionInfo(conn);
        return conn;
    }

    public void logConnectionInfo(Connection conn) throws SQLException {
        if (dbUrl == null) {
            DatabaseMetaData metaData = conn.getMetaData();
            dbUrl = metaData.getURL();
            logger.debug("url = " + dbUrl + " , " + metaData.getUserName());
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }
}

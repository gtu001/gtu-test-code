package gtu.jpa.hibernate;

import java.sql.Connection;
import java.sql.DriverManager;

public class ODBCDbUtils {
    /** * @param args */
    private String driver;
    private String url;
    private String username;
    private String password;
    private Connection conn;

    public ODBCDbUtils() {
        driver = "sun.jdbc.odbc.JdbcOdbcDriver";
        url = "jdbc:odbc:driver={Microsoft Excel Driver(*.xls)};DBQ=c:/test.xls";
        username = "";
        password = "";
    }

    public Connection getConnection() {
        if (conn == null)
            initConnection();
        return conn;
    }

    private void initConnection() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ODBCDbUtils().getConnection();
    }
}
package gtu.db.access.exp1;

/* 數據訪問組件 */

import gtu.db.access.exp2.testconn;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Troy 2009/02/02
 * 
 */
public class dbPool {
    private static dbPool instance = null;

    // 取得連接
    public static synchronized Connection getConnection() {
        if (instance == null) {
            instance = new dbPool();
        }
        return instance._getConnection();
    }

    private dbPool() {
        super();
    }

    private Connection _getConnection() {
        try {
            String sDBDriver = null;
            String sConnection = null;
            String sUser = null;
            String sPassword = null;

            Properties p = new Properties();

            InputStream is = getClass().getResourceAsStream("db.properties");
            p.load(is);
            sDBDriver = p.getProperty("DBDriver", sDBDriver);
            sConnection = p.getProperty("Connection", sConnection);
            sUser = p.getProperty("User", "");
            sPassword = p.getProperty("Password", "");

            String dbpath = testconn.class.getResource("loginDB.mdb").toURI().getPath().substring(1);
            System.out.println("######################################");
            System.out.println("sDBDriver:\t" + sDBDriver);
            System.out.println("sConnection:\t" + sConnection + dbpath);
            System.out.println("sUser:\t" + sUser);
            System.out.println("sPassword:\t" + sPassword);
            System.out.println("######################################");

            Properties pr = new Properties();
            pr.put("user", sUser);
            pr.put("password", sPassword);
            pr.put("characterEncoding", "BIG5");
            pr.put("useUnicode", "TRUE");

            Class.forName(sDBDriver).newInstance();
            return DriverManager.getConnection(sConnection, pr);

        } catch (Exception se) {
            se.printStackTrace();
            return null;
        }
    }

    // 釋放資源
    public static void dbClose(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        rs.close();
        ps.close();
        conn.close();

    }
}
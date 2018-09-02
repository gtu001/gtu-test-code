/*
 * Created on 2005/8/2
 */
package gtu.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Troy 2009/02/02
 * 
 */
public class DatabaseConnection {

    private static DatabaseConnection dbCon = null;

    public static DatabaseConnection getInstance() {
        if (dbCon == null)
            dbCon = new DatabaseConnection();
        return dbCon;
    }

    protected Connection getConnection() throws SQLException, ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        Connection conn = null;
        try {
            Properties pr = new Properties();
            pr.put("characterEncoding", "UTF8");
            pr.put("useUnicode", "TRUE");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql:\\\\localhost:3306\\cooperative", pr);
        } catch (SQLException se) {
            se.printStackTrace();
            throw se;
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
            throw ce;
        }

        return conn;
    }

    public static void main(String[] args) throws Exception {
        DatabaseConnection dbC = new DatabaseConnection();
        Connection con = dbC.getConnection();
        System.out.println(con);
        con.close();
        con = null;
        System.out.println(con);
    }
}

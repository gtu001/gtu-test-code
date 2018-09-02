package gtu.db.mysql;

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
public class DBPool {
    private static DBPool instance = null;

    public static synchronized Connection getConnection() {
        if (instance == null) {
            instance = new DBPool();
        }
        return instance._getConnection();
    }

    private DBPool() {
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
            sDBDriver = p.getProperty("driver", sDBDriver);
            sConnection = p.getProperty("url", sConnection);
            sUser = p.getProperty("user", "");
            sPassword = p.getProperty("password", "");

            String myConnection = sConnection + "?user=" + sUser + "&password=" + sPassword;

            Properties pr = new Properties();
            // pr.put("user",sUser);
            // pr.put("password",sPassword);
            pr.put("characterEncoding", "BIG5");
            pr.put("useUnicode", "TRUE");

            Class.forName(sDBDriver).newInstance();
            return DriverManager.getConnection(myConnection + "", pr);
        } catch (Exception se) {
            System.out.println(se);
            return null;
        }
    }

    public static void clearConn(PreparedStatement ps, ResultSet rs) {
        closeResultSet(rs);
        closePreparedStatement(ps);
    }

    public static void clearConn(PreparedStatement ps, Connection con) {
        // closeResultSet(rs);
        closePreparedStatement(ps);
        freeConncetion(con);
    }

    public static void clearConn(Connection con, PreparedStatement ps, ResultSet rs) {
        closeResultSet(rs);
        closePreparedStatement(ps);
        freeConncetion(con);
    }

    /**
     * 釋放連接池中的所有Connection
     * 
     * @throws SQLException
     */
    public static void freeConncetion(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException Ex_Connection) {
            System.out.println("SQLException when close Connection. " + Ex_Connection);
        }
    }

    /**
     * 釋放連接池中的所有ResultSet
     * 
     * @throws SQLException
     */
    public static void closeResultSet(ResultSet resultset) {
        try {
            if (resultset != null)
                resultset.close();
        } catch (SQLException Ex_Resultset) {
            System.out.println("SQLException when close ResultSet. " + Ex_Resultset);
        }
    }

    /**
     * 釋放連接池中的所有PreparedStatement
     * 
     * @throws SQLException
     */
    public static void closePreparedStatement(PreparedStatement ps) {
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException Ex_Statement) {
            System.out.println("SQLException when close PreparedStatement." + Ex_Statement);
        }
    }

    public static void rollBack(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException se) {
            System.out.println("SQLException when close rollBack." + se);
        }
    }

    public static void log(String s) {
        if (isDebug == true)
            System.out.println(s);
    }

    private static boolean isDebug = true;

    // public void delResume(String RID) throws SQLException {
    // connDel = DBPool.getConnection();
    // System.out.println("Here is DAO. Successful to connect to DB!!!");
    // try {
    // String sqlDel = "delete from resume where RID=?";
    // psDel = connDel.prepareStatement(sqlDel);
    // psDel.setInt(1, Integer.parseInt(RID));
    // psDel.executeUpdate();
    // sqlDel = "delete from workexpr where RID=?";
    // psDel = connDel.prepareStatement(sqlDel);
    // psDel.setInt(1, Integer.parseInt(RID));
    // psDel.executeUpdate();
    // sqlDel = "delete from studyexpr where RID=?";
    // psDel = connDel.prepareStatement(sqlDel);
    // psDel.setInt(1, Integer.parseInt(RID));
    // psDel.executeUpdate();
    // DBPool.clearConn(psDel, connDel);
    // } catch (SQLException e) {
    // System.out.println(e.getMessage());
    // }
    // }

}

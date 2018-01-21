package gtu.db.hook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Troy 2009/02/02
 * 
 */
public class TestConn {

    public static void main(String[] args) {
        // System.setProperty("CLASSPATH", "gtu.util");
        // System.setProperty("db.properties", "c:\\db.properties");

        for (int i = 1; i <= 1; i++) {
            System.out.println("count===>" + i);
            testDB();
            System.out.print("END------------------> " + i);
        }
    }

    /**
     * @param args
     */
    public static void testDB() {

        // TODO Auto-generated method stub
        DBConnectionManager connMgr = DBConnectionManager.getInstance();
        Connection con = connMgr.getConnection("fedidb");
        if (con == null) {
            System.out.println("cannot connect!");
            return;
        }
        ResultSet rs = null;
        ResultSetMetaData md = null;
        Statement stmt = null;
        try {
            String sqlstmt = " SELECT * FROM [FEDI_CUS]";
            stmt = con.createStatement();
            stmt.executeQuery(sqlstmt);

            rs = stmt.executeQuery(sqlstmt);
            md = rs.getMetaData();
            for (int i = 1; i < md.getColumnCount(); i++) {
                System.out.print(" " + md.getColumnName(i) + " ");
            }
            while (rs.next()) {
                for (int i = 1; i < md.getColumnCount(); i++) {
                    System.out.println(" " + rs.getString(i) + " ");
                }
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connMgr.freeConnection("gtu", con);
        System.out.println("END!");
    }

}

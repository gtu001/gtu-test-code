package gtu.db.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class EmbeddedConnectionTest {

    /**
     * @param args
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        Class.forName(driver).newInstance();

        Properties props = new Properties();
        //Connection conn = DriverManager.getConnection("jdbc:derby:c:/derbyDB;create=true", props); //XXX for create
        Connection conn = DriverManager.getConnection("jdbc:derby:c:/derbyDB", props); //XXX for use
        //Connection conn = DriverManager.getConnection("jdbc:derby:c:/derbyDB;shutdown=true", props); //XXX for shutdown
        conn.close();
        System.out.println("done...");
    }

}

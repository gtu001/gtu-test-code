package gtu.db.jdbc.informix;

import java.sql.SQLException;

import javax.sql.PooledConnection;

import com.informix.jdbcx.IfxConnectionPoolDataSource;

public class InformixDbConn {
    
    public static void main(String[] args) throws SQLException{
        PooledConnection conn = getConnection();
        conn.close();
        System.out.println("done..");
    }

    // jdbc:informix-sqli://192.168.10.18:4526/teun0020:informixserver=aix2;DB_LOCALE=zh_tw.utf8;CLIENT_LOCALE=zh_tw.utf8;GL_USEGLU=1
    public static PooledConnection getConnection() {
        IfxConnectionPoolDataSource cpds;
        try {
            String DB_HOST = "192.168.10.18";
            int DB_PORT = 4526;
            String DB_USER = "srismapp";
            String DB_PWD = "ris31123";
            String DB_SERVER_NAME = "aix2";
//            String DB_NAME = "teun0020";//RL XXX
            String DB_NAME = "chun0000";//RC XXX
            String DB_LOCALE = "zh_tw.utf8";
            String DB_CLIENT_LOCALE = "zh_tw.utf8";

            cpds = new IfxConnectionPoolDataSource();
            cpds.setDescription("Pick-A-Seat Connection pool");
            cpds.setIfxCPMMaxConnections(-1);
            cpds.setIfxCPMMaxPoolSize(10);
            cpds.setIfxCPMMinPoolSize(5);

            cpds.setIfxIFXHOST(DB_HOST);
            cpds.setPortNumber(DB_PORT);
            cpds.setUser(DB_USER);
            cpds.setPassword(DB_PWD);
            cpds.setServerName(DB_SERVER_NAME);
            cpds.setDatabaseName(DB_NAME);
            cpds.setIfxCLIENT_LOCALE(DB_CLIENT_LOCALE);
            cpds.setIfxDB_LOCALE(DB_LOCALE);

            return cpds.getPooledConnection();
        } catch (Exception sqle) {
            throw new RuntimeException(sqle);
        }
    }
}

package gtu.zcognos;

import gtu.collection.ListUtil;
import gtu.zcognos.DimensionUI.Dimension_;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.PooledConnection;

import com.informix.jdbcx.IfxConnectionPoolDataSource;

public class InformixDbConn {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<Dimension_> list = InformixDbConn.queryGetDaminsion("rscdpg0901", "RCRP0C2H2");
        ListUtil.showListInfo(list);
    }

    static List<Dimension_> queryGetDaminsion(String tableName, String reportId) {
        List<Dimension_> list = new ArrayList<Dimension_>();
        try {
            Connection conn = getConnection().getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT DISTINCT category, remark FROM %s WHERE report_id = '%s'", tableName, reportId);
            ResultSet rs = stmt.executeQuery(sql);
            Dimension_ ddd = null;
            int index = 2;
            while (rs.next()) {
                ddd = new Dimension_();
                ddd.category = rs.getString(1);
                ddd.dimensionName = rs.getString(2);
                ddd.dataSourceTable = tableName;
                ddd.idIndex = "id" + index;
                ddd.tableName = DimensionUI.randomTableName();
                ddd.reportId = reportId;
                list.add(ddd);
                index++;
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static PooledConnection getConnection() {
        IfxConnectionPoolDataSource cpds;
        try {
            String DB_HOST = "192.168.10.18";
            int DB_PORT = 4526;
            String DB_USER = "srismapp";
            String DB_PWD = "ris31123";
            String DB_SERVER_NAME = "aix2";
            String DB_NAME = "teun0020";
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

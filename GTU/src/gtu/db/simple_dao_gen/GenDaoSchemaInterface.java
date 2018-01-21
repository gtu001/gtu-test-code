package gtu.db.simple_dao_gen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.JdbcUtils;

import gtu.string.StringUtilForDb;

public class GenDaoSchemaInterface {

    public String execute(String tableName, List<String> colList) throws SQLException {
        String methodResult = getInterfaceClass(tableName, colList);
        System.out.println(methodResult);
        return methodResult;
    }

    private List<String> getColumnList(String tableName, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select * from " + tableName + " where 1!=1 ");
        ResultSetMetaData metaData = stmt.getMetaData();
        List<String> colList = new ArrayList<String>();
        for (int ii = 1; ii <= metaData.getColumnCount(); ii++) {
            String columnLabel = metaData.getColumnLabel(ii);
            colList.add(columnLabel);
        }
        JdbcUtils.closeConnection(conn);
        return colList;
    }

    private String getInterfaceClass(String tableName, List<String> colList) {
        StringBuilder sb = new StringBuilder();
        String interfaceName = StringUtils.capitalize(StringUtilForDb.dbFieldToJava(tableName));
        sb.append("    public interface " + interfaceName + "_schema {           \n");
        for (int ii = 0; ii < colList.size(); ii++) {
            String colName = colList.get(ii).toUpperCase();
            sb.append("        public static final String " + colName + " = \"" + colName + "\";      \n");
        }
        sb.append("    }                                                       \n");
        return sb.toString();
    }
}

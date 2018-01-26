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

public class GenDaoAllMain {

    public String execute(String tableName, List<String> colList, List<String> pkList, Connection conn) throws SQLException {
        colList = toUpperCase(colList);
        pkList = toUpperCase(pkList);
        String className = StringUtils.capitalize(StringUtilForDb.dbFieldToJava(tableName));
        
        StringBuilder sb = new StringBuilder();

        sb.append(new GenDaoBean().execute("select * from " + tableName + " where 1!=1", tableName, conn));
        
        sb.append("public class " + className + "Dao {\n");
        sb.append(new GenDaoSchemaInterface().execute(tableName, colList));
        sb.append(new GenDaoInsertMethod().execute(tableName, colList));
        sb.append(new GenDaoDeleteMethod().execute(tableName, pkList));
        sb.append(new GenDaoUpdateMethod().execute(tableName, colList, pkList));
        sb.append(new GenDaoSelectMethod().execute(tableName, pkList));
        sb.append(new GenDaoTruncateMethod().execute(tableName));
        sb.append("}\n");
        
        return sb.toString();
    }

    private List<String> toUpperCase(List<String> colList) {
        for (int ii = 0; ii < colList.size(); ii++) {
            String col = colList.get(ii).toUpperCase();
            colList.set(ii, col);
        }
        return colList;
    }

    public List<String> getColumnList(String tableName, Connection conn) throws SQLException {
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
}

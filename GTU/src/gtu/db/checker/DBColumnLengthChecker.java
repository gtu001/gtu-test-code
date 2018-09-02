
package gtu.db.checker;

import gtu.db.DBColumnType;
import gtu.db.sqlMaker.DbSqlCreater.FieldInfo4DbSqlCreater;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class DBColumnLengthChecker {

    public static void main(String[] args) {
        DBColumnLengthChecker t = new DBColumnLengthChecker();
        String sql = "";
        Connection conn = null;
        Map<String, FieldInfo4DbSqlCreater> columnInfo = t.getColumnInfo(sql, conn);
        int columnLength = columnInfo.get("column_name_here").getColumnDisplaySize();
    }

    public Map<String, FieldInfo4DbSqlCreater> getColumnInfo(String sql, Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (conn instanceof oracle.jdbc.driver.OracleConnection) {
                ((oracle.jdbc.driver.OracleConnection) conn).setRemarksReporting(true);
            }

            Map<String, FieldInfo4DbSqlCreater> columnInfo = new LinkedHashMap<String, FieldInfo4DbSqlCreater>();

            ResultSetMetaData meta = rs.getMetaData();
            String colLabel = null;
            for (int ii = 1; ii <= meta.getColumnCount(); ii++) {
                colLabel = meta.getColumnLabel(ii).toLowerCase();

                FieldInfo4DbSqlCreater finfo = new FieldInfo4DbSqlCreater();
                finfo.setCatalogName(meta.getCatalogName(ii));
                finfo.setColumnClassName(meta.getColumnClassName(ii));
                finfo.setColumnDisplaySize(meta.getColumnDisplaySize(ii));
                finfo.setColumnLabel(meta.getColumnLabel(ii));
                finfo.setColumnName(meta.getColumnName(ii));
                finfo.setColumnType(meta.getColumnType(ii));
                finfo.setColumnTypeName(meta.getColumnTypeName(ii));
                finfo.setPrecision(meta.getPrecision(ii));
                finfo.setScale(meta.getScale(ii));
                finfo.setSchemaName(meta.getSchemaName(ii));
                finfo.setTableName(meta.getTableName(ii));
                finfo.setAutoIncrement(meta.isAutoIncrement(ii));
                finfo.setCaseSensitive(meta.isCaseSensitive(ii));
                finfo.setCurrency(meta.isCurrency(ii));
                finfo.setDefinitelyWritable(meta.isDefinitelyWritable(ii));
                finfo.setNullable(meta.isNullable(ii));
                finfo.setReadOnly(meta.isReadOnly(ii));
                finfo.setSearchable(meta.isSearchable(ii));
                finfo.setSigned(meta.isSigned(ii));
                finfo.setWritable(meta.isWritable(ii));
                finfo.setColumnTypeZ(DBColumnType.lookup(meta.getColumnType(ii)));
                columnInfo.put(colLabel, finfo);
            }

            return columnInfo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
            }
            try {
                stmt.close();
            } catch (SQLException e) {
            }
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }
}

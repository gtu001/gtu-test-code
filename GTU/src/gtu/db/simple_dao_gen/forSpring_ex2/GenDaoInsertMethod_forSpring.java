package gtu.db.simple_dao_gen.forSpring_ex2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.JdbcUtils;

public class GenDaoInsertMethod_forSpring {
    
    public String execute(String tableName, List<String> colList) throws SQLException{
        String sql = this.createInsertSql(tableName, colList);
        String methodResult = getInsertMethod(sql, colList);
        System.out.println(methodResult);
        return methodResult;
    }
    
    private List<String> getColumnList(String tableName, Connection conn) throws SQLException{
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

    private String createInsertSql(String tableName, List<String> columns) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("INSERT INTO %s  (", tableName));
        for (String key : columns) {
            sb.append(key + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") VALUES ( ");
        for (String key : columns) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") ");
        return sb.toString();
    }
    
    private String getInsertMethod(String sql, List<String> colList) {
        StringBuilder sb = new StringBuilder();
        sb.append("    public int insert(Map<String,Object> valMap) {       \n");
        sb.append("        try {                                                                   \n");
        sb.append("            String sql = \"" + sql + "\";\n");
        sb.append("            Object[] params = new Object[] { //                                   \n");
        
        for (int ii = 0; ii < colList.size(); ii++) {
            String colName = colList.get(ii);
            sb.append("             valMap.get(\"" + colName + "\"), // \n");
        }
        
        sb.append("            };                                                                  \n");
        sb.append("            return template.update(sql, params);                                \n");
        sb.append("        }catch(Exception ex) {                                                  \n");
        sb.append("            throw new RuntimeException(ex);                                     \n");
        sb.append("        }finally {                                                              \n");
        sb.append("        }                                                                       \n");
        sb.append("    }                                                                           \n");
        return sb.toString();
    }
}

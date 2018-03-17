package gtu.db.simple_dao_gen.forSpring_ex2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.JdbcUtils;


public class GenDaoUpdateMethod_forSpring {
    
    public String execute(String tableName, List<String> colList, List<String> pkList) throws SQLException{
        String sql = this.createUpdateSql(tableName, colList, pkList);
        String methodResult = getUpdateMethod(sql, colList, pkList);
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

    public String createUpdateSql(String tableName, List<String> colList, List<String> pkList) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE " + tableName + " SET ");
        for (String key : colList) {
            String key_ = key.toUpperCase();
            sb.append(key + "=?" + ",");
        }
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(" WHERE ");
        for (String key : pkList) {
            String key_ = key.toUpperCase();
            sb.append(" " + key + "=? and");
        }
        if (pkList.size() != 0) {
            sb.delete(sb.length() - 4, sb.length());
        }
        return sb.toString();
    }
    

    private String getUpdateMethod(String sql, List<String> colList, List<String> pkList) {
        StringBuilder sb = new StringBuilder();
        sb.append("    public int update(Map<String,Object> valMap, Map<String,Object> pkMap) {      \n");
        sb.append("        try {                                                                                            \n");
        sb.append("            String sql = \"" + sql + "\";\n");
        sb.append("            Object[] params = new Object[] { //                                                            \n");
        
        int jj = 0;
        for (int ii = 0; ii < colList.size(); ii++, jj++) {
            String colName = colList.get(ii);
            sb.append("             valMap.get(\"" + colName + "\"),// \n");
        }
        for (int ii = 0; ii < pkList.size(); ii++, jj++) {
            String colName = pkList.get(ii);
            sb.append("             pkMap.get(\"" + colName + "\"),// \n");
        }
        
        sb.append("            };                                                                                           \n");
        sb.append("            return template.update(sql, params);                                                         \n");
        sb.append("        }catch(Exception ex) {                                                                           \n");
        sb.append("            throw new RuntimeException(ex);                                                              \n");
        sb.append("        }finally {                                                                                       \n");
        sb.append("        }                                                                                                \n");
        sb.append("    }                                                                                                    \n");
        return sb.toString();
    }
}

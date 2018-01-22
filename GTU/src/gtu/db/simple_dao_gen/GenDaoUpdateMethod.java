
package gtu.db.simple_dao_gen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.JdbcUtils;


public class GenDaoUpdateMethod {
    
    public String execute(String tableName, List<String> colList, List<String> pkList) throws SQLException{
        tableName = StringUtils.trimToEmpty(tableName);
        GenDaoUpdateMethod.trimStringList(colList);
        GenDaoUpdateMethod.trimStringList(pkList);
        String sql = this.createUpdateSql(tableName, colList, pkList);
        String methodResult = getUpdateMethod(sql, colList, pkList);
        System.out.println(methodResult);
        return methodResult;
    }
    
    static void trimStringList(List<String> lst){
        for(int ii = 0 ; ii < lst.size() ; ii ++) {
            String v = StringUtils.trimToEmpty(lst.get(ii));
            lst.set(ii, v);
        }
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
        sb.append("    public int update(Map<String,Object> valMap, Map<String,Object> pkMap, Connection conn) {     \n");
        sb.append("        PreparedStatement stmt = null;                                    \n");
        sb.append("        try {                                                             \n");
        sb.append("            conn.setAutoCommit(false);                                    \n");
        sb.append("            String sql = \"" + sql + "\";\n");
        sb.append("            stmt = conn.prepareStatement(sql);                            \n");
        sb.append("                                                                          \n");
        int jj = 0;
        for (int ii = 0; ii < colList.size(); ii++, jj++) {
            String colName = colList.get(ii);
            sb.append("            stmt.setObject(" + (jj + 1) + ", valMap.get(\"" + colName + "\")); \n");
        }
        for (int ii = 0; ii < pkList.size(); ii++, jj++) {
            String colName = pkList.get(ii);
            sb.append("            stmt.setObject(" + (jj + 1) + ", pkMap.get(\"" + colName + "\")); \n");
        }
        sb.append("                                                                          \n");
        sb.append("            int result = stmt.executeUpdate();                            \n");
        sb.append("            System.out.println(\"update result : \" + result);            \n");
        sb.append("            // conn.commit();                                             \n");
        sb.append("            return result;                                                \n");
        sb.append("        } catch (Exception e) {                                           \n");
        sb.append("            e.printStackTrace();                                          \n");
        sb.append("            try {                                                         \n");
        sb.append("                conn.rollback();                                          \n");
        sb.append("            } catch (Exception e1) {                                   \n");
        sb.append("                e1.printStackTrace();                                     \n");
        sb.append("            }                                                             \n");
        sb.append("            throw new RuntimeException(e);                                \n");
        sb.append("        } finally {                                                       \n");
        sb.append("            try{                                                          \n");
        sb.append("                stmt.close();                                             \n");
        sb.append("            }catch(Exception ex){                                         \n");
        sb.append("                ex.printStackTrace();                                     \n");
        sb.append("            }                                                             \n");
        sb.append("        }                                                                 \n");
        sb.append("    }                                                                     \n");
        return sb.toString();
    }
}
